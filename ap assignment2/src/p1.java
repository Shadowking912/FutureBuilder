import java.util.*;

class admin{
    public Scanner scanner=new Scanner(System.in);
    private String username;
    private String pass;


    public admin(String usr,String passw){
        this.username=usr;
        this.pass=passw;
    }
    public void addcategory(Map<String,category> categories,Map<String,product> products){
        System.out.print("Add Id of category: ");
        int y=scanner.nextInt();
        if(categories.get(Integer.toString(y))!=null){
            System.out.println("id exists");
            return;
        }
        scanner.nextLine();
        System.out.print("Add Name of category: ");
        String s=scanner.nextLine();
        int flag=0;
        for(String f:categories.keySet()){
            if(categories.get(f).getcat_name().equals(s)){
                System.out.println("category exists with id "+f);
                return;
            }
        }
        category c=new category();
        c.setcat_id(y);
        c.setcat_name(s);
        this.addprod(c,products);
        categories.put(Integer.toString(y),c);
    }
    public void deletecategory(Map<String,category> categories) {
        System.out.print("Enter Id of category: ");
        String y = scanner.nextLine();
        System.out.print("Enter Name of category: ");
        String s = scanner.nextLine();
        if (categories.get(y) != null && categories.get(y).getcat_name().equals(s)) {
            categories.remove(y);
            System.out.println("Category removed");
        }
        else {
            System.out.println("No such category exists");
        }
    }
    public void addprod(category c,Map<String,product> products){
        System.out.println("\nAdd product");
        System.out.print("Product Name: ");
        String s=scanner.nextLine();
        System.out.print("Product id: ");
        double z=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Price: ");
        double price= scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Quantity: ");
        int qty=scanner.nextInt();
        scanner.nextLine();
        System.out.println("enter additional details comma separated");
        String x=scanner.nextLine();
        c.addproduct(s,z,x,price,qty,products);

    }
    public void generatecoupon(){

    }
    public String getpass(){
        return this.pass;
    }


    public void removecategory(Map<String,category> categories,category flag,Map<String,product> products) {
        for(int i=0;i<flag.getprod().size();i++){
            System.out.println(flag.getprod().get(i).getProductid());
            products.remove(Double.toString(flag.getprod().get(i).getProductid()));
        }
        categories.remove(flag);
    }
}
class cartitems{
    item it;
    int quantity;
    cartitems(item a,int qty){
        this.it=a;
        this.quantity=qty;
    }
}
class customer{
    private String name;
    private String pass;
    private double balance;
    private String status;
    private ArrayList<cartitems> cart=new ArrayList<cartitems>();
    private PriorityQueue<Double> coupons=new PriorityQueue<Double>(Collections.reverseOrder());
    Scanner scanner=new Scanner(System.in);
    Random r=new Random();

    public customer(String n,String p){
        this.name=n;
        this.pass=p;
        this.balance=1000;
        this.status="Normal";
    }
    public void addproduct(item p,int qty){
            cart.add(new cartitems(p, qty));

    }
    public double getbalance() {
        return this.balance;
    }

    public void displaycart() {
        if(this.cart.isEmpty()){
            System.out.println("Cart empty");
        }
        else {
            for (int i=0;i<cart.size();i++) {
                item p=cart.get(i).it;
                p.getdetails(this,cart.get(i).quantity);
            }
        }
    }

    public void emptycart(){
        this.cart.clear();
    }


    public void upgradestatus() {
        System.out.println("Current status: "+this.status);
        System.out.print("Choose new status(Normal,Prime,Elite): ");
        String s=scanner.nextLine();
        if(this.status.equals("Normal")){
            if(s.equals("Prime")){
                if(this.balance>=200){
                    this.balance-=200;
                    this.status="Prime";
                    System.out.println("Status upgraded to Prime");
                }
                else{
                    System.out.println("Insufficient balance");
                }
            }
            else if(s.equals("Elite")){
                if(this.balance>=300){
                    this.balance-=300;
                    this.status="Elite";
                    System.out.println("Status upgraded to Elite");
                }
                else{
                    System.out.println("Insufficient balance");
                }
            }

        }
        else if(this.status.equals("Prime")){
            if(s.equals("Elite")){
                if(this.balance>=100){
                    this.balance-=100;
                    this.status="Elite";
                    System.out.println("Status upgraded to Elite");
                }
                else{
                    System.out.println("Insufficient balance");
                }
            }
        }
        else{
            System.out.println("Maximum status reached,cannot downgrade");
        }
    }

    public void addamount() {
        System.out.print("Enter amount to add: ");
        double y=scanner.nextDouble();
        scanner.nextLine();
        this.balance=this.balance+y;
        System.out.println("Amount added");
    }

    public String getpass() {

        return this.pass;
    }

    public void adddeal(item d){
        cart.add(new cartitems(d,1));
        System.out.println("deal added to cart");
    }
    public double ncheckout(Map<String,product> products){
        double price=0;
        double c=-1;
        int flag=0;
        cartitems[] items=cart.toArray(new cartitems[0]);
        for(int i=0;i<items.length;i++){
            if(items[i].it.getClass()==product.class) {
                if(products.get(Double.toString(((product)items[i].it).getProductid()))==null){
                    System.out.println(((product) items[i].it).getProdcutname() + " has been deleted");
                    return -1;
                }
                else if (cart.get(i).quantity > ((product)items[i].it).getqty()) {
                    System.out.println("Quantity of " + ((product) items[i].it).getProdcutname() + " not available");
                    return -1;
                }
                else {
                    if (coupons.isEmpty() == false) {
                        if (coupons.peek() > items[i].it.getdiscount()[0]) {
                            c=coupons.peek();
                            flag=1;
                            price = price + ((product) items[i].it).getprice() * (100 - c) * cart.get(i).quantity / 100;
                        } else {
                            price = price + ((product) items[i].it).getprice() * (100 - items[i].it.getdiscount()[0]) * cart.get(i).quantity/ 100;
                        }
                    } else {
                        price = price + ((product) items[i].it).getprice() * (100 - items[i].it.getdiscount()[0]) * cart.get(i).quantity / 100;
                    }
                }
            }
            else{
                if(products.get(Double.toString(((deal) items[i].it).getp1().getProductid()))==null || products.get(Double.toString(((deal) items[i].it).getp2().getProductid()))==null){
                    System.out.println("A product in the deal has been deleted");
                    return -1;
                }
                else if (((deal) items[i].it).getp1().getqty()>0 && ((deal) items[i].it).getp2().getqty()>0) {

                    price=price+items[i].it.getdiscount()[0];
                }
                else{
                    System.out.println("Quantity of " + ((deal) items[i].it).getdealid() + " not available");
                }
            }
        }
        if(price<=this.balance && flag==1){
            coupons.poll();
        }
        return price;
    }
    public double pcheckout(Map<String,product> products){
        double price=0;
        double c=-1;
        int flag=0;
        cartitems[] items=cart.toArray(new cartitems[0]);
        for(int i=0;i<items.length;i++){
            if(items[i].it.getClass()==product.class) {
                if(products.get(Double.toString(((product)items[i].it).getProductid()))==null){
                    System.out.println(((product) items[i].it).getProdcutname() + " has been deleted");
                    return -1;
                }
                else if (cart.get(i).quantity > ((product) items[i].it).getqty()) {
                    System.out.println("Quantity of " + ((product) items[i].it).getProdcutname() + " not available");
                    return -1;
                }
                else {
                    if (coupons.isEmpty() == false) {
                        Double[] z = {items[i].it.getdiscount()[1], 5.0};
                        List<Double> f = new ArrayList<Double>(Arrays.asList(z));
                        double finaldisocunt = Collections.max(f);
                        if (coupons.peek() > finaldisocunt) {
                            flag=1;
                            c=coupons.peek();
                            price = price + ((product) items[i].it).getprice() * (100 - c) * cart.get(i).quantity / 100;
                        } else {
                            price = price + ((product) items[i].it).getprice() * (100 - finaldisocunt) * cart.get(i).quantity / 100;
                        }
                    }
                    else {
                        Double[] z = {items[i].it.getdiscount()[1], 5.0};
                        List<Double> f = new ArrayList<Double>(Arrays.asList(z));
                        double finaldisocunt = Collections.max(f);
                        price = price + ((product) items[i].it).getprice() * (100 - finaldisocunt) * cart.get(i).quantity / 100;
                    }
                }
            }
            else{
                if(products.get(Double.toString(((deal) items[i].it).getp1().getProductid()))==null || products.get(Double.toString(((deal) items[i].it).getp2().getProductid()))==null){
                    System.out.println("A product in the deal has been deleted");
                    return -1;
                }
                else if (((deal) items[i].it).getp1().getqty()>0 && ((deal) items[i].it).getp2().getqty()>0) {

                    price=price+items[i].it.getdiscount()[1];
                }
                else{
                    System.out.println("Quantity of " + ((deal) items[i].it).getdealid() + " not available");
                }

            }
        }
        if(price<=this.balance && flag==1){
            coupons.poll();
        }
        else if(price>5000){
            int y=r.nextInt(1,3);
            for(int i=0;i<y;i++){
                coupons.add(r.nextDouble(5,15));
            }
        }
        return price;
    }
    public double echeckout(Map<String,product> products){

        double c=-1;
        double price=0;
        int flag=0;
        cartitems[] items=cart.toArray(new cartitems[0]);
        for(int i=0;i<items.length;i++){
            if(items[i].it.getClass()==product.class) {
                if(products.get(Double.toString(((product)items[i].it).getProductid()))==null){
                    System.out.println(((product) items[i].it).getProdcutname() + " has been deleted");
                    return -1;
                }
                else if (cart.get(i).quantity > ((product) items[i].it).getqty()) {
                    System.out.println("Quantity of " + ((product) items[i].it).getProdcutname() + " not available");
                    return -1;
                }
                else {
                    if (coupons.isEmpty() == false) {
                        Double[] z = {items[i].it.getdiscount()[2], 10.0};
                        List<Double> f = new ArrayList<Double>(Arrays.asList(z));
                        double finaldisocunt = Collections.max(f);
                        if (coupons.peek()> finaldisocunt) {
                            c= coupons.peek();
                            price = price + ((product) items[i].it).getprice() * (100 - c) *cart.get(i).quantity / 100;
                            flag=1;
                        } else {
                            price = price + ((product) items[i].it).getprice() * (100 - finaldisocunt) * cart.get(i).quantity / 100;
                        }
                    }
                    else {
                        Double[] z = {items[i].it.getdiscount()[2], 10.0};
                        List<Double> f = new ArrayList<Double>(Arrays.asList(z));
                        double finaldisocunt = Collections.max(f);
                        price = price + ((product) items[i].it).getprice() * (100 - finaldisocunt) * cart.get(i).quantity / 100;
                    }
                }
            }
            else{
                if(products.get(Double.toString(((deal) items[i].it).getp1().getProductid()))==null || products.get(Double.toString(((deal) items[i].it).getp2().getProductid()))==null){
                    System.out.println("A product in the deal has been deleted");
                    return -1;
                }
                else if (((deal) items[i].it).getp1().getqty()>0 && ((deal) items[i].it).getp2().getqty()>0) {

                    price=price+items[i].it.getdiscount()[2];
                }
                else{
                    System.out.println("Quantity of " + ((deal) items[i].it).getdealid() + " not available");
                }

            }
        }
        if(price<=this.balance && flag==1){
            coupons.poll();
        }
        if(price>5000){
            int y=r.nextInt(3,5);
            for(int i=0;i<y;i++){
                coupons.add(r.nextDouble(5,15));
            }
        }
        return price;
    }

    public void deductbalance(double price) {
        for(int i=0;i<cart.size();i++){
            if(cart.get(i).it.getClass()==product.class){
                ((product)cart.get(i).it).deductqty(cart.get(i).quantity);
            }
        }
        this.balance=this.balance-price;
    }

    public String getstatus() {
        return this.status;
    }

    public ArrayList<cartitems> getcart() {
        return this.cart;
    }

    public PriorityQueue<Double> getcoupons() {
        return this.coupons;
    }
}

class product implements item{
    private String Prodcutname;
    private double Productid;
    private String info;
    private double price;
    private double ndiscount;
    private double ediscount;
    private double pdiscount;
    private int qty;
    public product(String pname,double pid,String pinfo,double p_price,int pqty){
        this.Prodcutname=pname;
        this.Productid=pid;
        this.info=pinfo;
        this.pdiscount=0;
        this.ediscount=0;
        this.ndiscount=0;
        this.price=p_price;
        this.qty=pqty;
    }

    public double getpdiscount() {
        return this.pdiscount;
    }

    public double getndiscount() {
        return this.ndiscount;
    }

    public double getediscount() {
        return this.ediscount;
    }

    public void setpdiscount(double pri) {
        this.pdiscount=pri;
    }

    public void setediscount(double e) {
        this.ediscount=e;
    }

    public void setndiscount(double n) {
        this.ndiscount=n;
    }

    @Override
    public void getdetails(customer c,int quantity) {
        System.out.println("Product name: "+this.Prodcutname);
        System.out.println("Product id: "+this.Productid);
        System.out.println("Price: "+this.price);
        System.out.println("Quantity: "+quantity);
        String[] s = this.info.split(",");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
        System.out.println();
    }
    @Override
    public double[] getdiscount(){
        return new double[]{this.ndiscount, this.pdiscount, this.ediscount};
    }

    public double getProductid() {
        return this.Productid;
    }

    public String getProdcutname() {
        return this.Prodcutname;
    }

    public int getqty() {
        return this.qty;
    }

    public double getprice() {
        return this.price;
    }

    public void deductqty(int quantity) {
        this.qty=this.qty-quantity;
    }

    public String getinfo() {
        return this.info;
    }
}
class category{
    private int cat_id;
    private String cat_name;
    private ArrayList<product> prod=new ArrayList<product>();
    public void addproduct(String pname,double pid,String info,double price,int qty,Map<String,product> products){
        product p=new product(pname,pid,info,price,qty);
        prod.add(p);
        products.put(Double.toString(pid),p);
    }

    public String getcat_id() {
        return Integer.toString(this.cat_id);
    }

    public ArrayList<product> getprod() {
        return this.prod;
    }

    public void setcat_id(int y) {
        this.cat_id=y;
    }

    public String getcat_name() {
        return this.cat_name;
    }

    public void setcat_name(String s) {
        this.cat_name=s;
    }
}
class deal implements item{
    private String dealid;
    private product p1;
    private product p2;
    private double nprice;
    private double pprice;
    private double eprice;
    public deal(String id,product a,product b,double n,double p,double e){
        this.dealid=id;
        this.p1=a;
        this.p2=b;
        this.nprice=n;
        this.pprice=p;
        this.eprice=e;
    }

    @Override
    public void getdetails(customer c,int quantity) {
        System.out.println("Dealid: "+this.dealid);
        if(c.getstatus().equals("Normal")){
            System.out.println("Deal price: "+this.nprice);
        }
        else if(c.getstatus().equals("Prime")){
            System.out.println("Deal price: "+this.pprice);
        }
        else if(c.getstatus().equals("Elite")){
            System.out.println("Deal price: "+this.eprice);
        }
        System.out.println("First product");
        System.out.println("Product name: "+p1.getProdcutname());
        System.out.println("Product id: "+p1.getProductid());
        String[] s = p1.getinfo().split(",");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
        System.out.println();
        System.out.println("Second product: ");
        System.out.println(p2.getProdcutname());
        System.out.println(p2.getProductid());
        String[] s2 = p2.getinfo().split(",");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s2[i]);
        }
        System.out.println();
    }
    @Override
    public double[] getdiscount(){
        return new double[]{this.nprice, this.pprice, this.eprice};
    }

    public product getp1() {
        return this.p1;
    }
    public product getp2(){
        return this.p2;
    }

    public String getdealid() {
        return this.dealid;
    }

    public Double getnprice() {
        return this.nprice;
    }

    public Double getpprice() {
        return this.pprice;
    }
    public Double geteprice() {
        return this.eprice;
    }
}
interface item{
    void getdetails(customer c,int quantity);
    double[] getdiscount();
}
public class p1{
    public static Scanner scanner=new Scanner(System.in);
    private static Map<String,category> categories=new HashMap<String,category>();
    private static Map<String,admin> admins=new HashMap<String,admin>();
    private static Map<String,product> products=new HashMap<String,product>();
    private static Map<String,customer> customers=new HashMap<String,customer>();
    private static Map<String,deal> deallist=new HashMap<String,deal>();
    public static void main(String[] args){
        admin a1=new admin("Karan","123");
        admin a2=new admin("jeff","123");
        admins.put("karan",a1);
        admins.put("jeff",a2);
        while(true) {
            System.out.println("\nWELCOME TO FLIPZON\n");
            System.out.println("1) Enter as Admin");
            System.out.println("2) Explore the Product Catalog");
            System.out.println("3) Show available deals");
            System.out.println("4) Enter as Customer");
            System.out.println("5) Exit the application");
            System.out.print("Enter the choice: ");
            int x = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            if (x == 1) {
                admin_menu();
            } else if (x == 2) {
                displayproducts();
            }
            else if(x==3){
                displaydeals();
            } 
            else if (x == 4) {
                customer_menu();
            } else if (x == 5) {
                System.out.println("thanks for using flipzon");
                break;
            }
        }
    }
    public static void displayproducts(){
        if(products.isEmpty()){
            System.out.println("No products available");
        }
        else {
            for (product p : products.values()) {
                System.out.println("Product name: "+p.getProdcutname());
                System.out.println("Product id: "+p.getProductid());
                System.out.println("Price: "+p.getprice());
                String[] s = p.getinfo().split(",");
                for (int i = 0; i < s.length; i++) {
                    System.out.println(s[i]);
                }
                System.out.println();
            }
        }
    }
    public static void displaydeals(){
        deal[] deals=deallist.values().toArray(new deal[0]);
        for(int i=0;i<deals.length;i++) {
            deal d=deals[i];
            System.out.println("Dealid: " + d.getdealid());
            System.out.println("Deal price(Normal Prime Elite): " + d.getnprice()+" "+d.getpprice()+" "+d.geteprice());
            System.out.println("First product");
            System.out.println("Product name: " + d.getp1().getProdcutname());
            System.out.println("Product id: " + d.getp1().getProductid());
            String[] s = d.getp1().getinfo().split(",");
            for (int j = 0; j < s.length; j++) {
                System.out.println(s[j]);
            }
            System.out.println();
            System.out.println("Second product: ");
            System.out.println(d.getp2().getProdcutname());
            System.out.println(d.getp2().getProductid());
            String[] s2 = d.getp2().getinfo().split(",");
            for (int j = 0; j < s.length; j++) {
                System.out.println(s2[j]);
            }
            System.out.println();
        }
    }
    public static void admin_menu(){
        System.out.println("Dear admin,");
        System.out.print("Enter name: ");
        String name=scanner.nextLine();
        if(admins.get(name)!=null) {
            admin ad = admins.get(name);
            System.out.print("Enter pass: ");
            String pass= scanner.nextLine();
            if (ad.getpass().equals(pass)) {
                while (true) {
                    System.out.println("\nWelcome " + name);
                    System.out.println("Choose any of the following actions: ");
                    System.out.println("1) Add category");
                    System.out.println("2) Delete category");
                    System.out.println("3) Add Product");
                    System.out.println("4) Delete Product");
                    System.out.println("5) Set Discount on Product");
                    System.out.println("6) Add giveaway deals");
                    System.out.println("7) Back\n");
                    System.out.print("Enter choice: ");
                    int x = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();

                    if (x == 1) {
                        ad.addcategory(categories,products);
                    }

                    else if (x == 2) {
                        ad.deletecategory(categories);
                    }

                    else if (x == 3) {
                        System.out.print("Enter category id: ");
                        int z = scanner.nextInt();
                        scanner.nextLine();
                        category c = categories.get(Integer.toString(z));
                        if (c == null) {
                            System.out.println("category does not exist create category first");
                        } else {
                            ad.addprod(c,products);
                        }

                    }

                    else if (x == 4) {
                        System.out.print("enter category name: ");
                        String s = scanner.nextLine();
                        category flag = null;
                        for (category y : categories.values()) {
                            if (y.getcat_name().equals(s)) {
                                flag = y;
                                break;
                            }
                        }
                        if (flag == null) {
                            System.out.println("no such category exists");
                        }
                        else {
                            System.out.print("Enter product id: ");
                            double y = scanner.nextDouble();
                            scanner.nextLine();
                            if (flag.getprod().removeIf(n -> (n.getProductid() == y))) {
                                products.remove(Double.toString(y));
                                System.out.println("Product " + y + " removed");
                                if (flag.getprod().size() == 0) {
                                    System.out.print("category empty enter y-delete category n-add product: ");
                                    s = scanner.nextLine();
                                    if (s.equals("y")) {
                                        categories.remove(flag.getcat_id());
                                        System.out.println("category removed");
                                    }
                                    else if(s.equals("n")){
                                        ad.addprod(flag,products);
                                    }
                                }
                            }
                            else {
                                System.out.println("no such product exists");
                            }
                        }

                    }

                    else if (x == 5) {
                        System.out.print("Enter product id: ");
                        double z = scanner.nextDouble();
                        scanner.nextLine();
                        product p = products.get(Double.toString(z));
                        System.out.println("Enter discount for elite,prime,normal customer space separated (in %)");
                        int e = scanner.nextInt();
                        int pri = scanner.nextInt();
                        int n = scanner.nextInt();
                        scanner.nextLine();
                        p.setpdiscount(pri);
                        p.setediscount(e);
                        p.setndiscount(n);
                    }

                    else if (x == 6) {
                        System.out.print("Enter dealid: ");
                        String id=scanner.nextLine();
                        System.out.print("Enter first product id: ");
                        double a=scanner.nextDouble();
                        scanner.nextLine();
                        product p1=products.get(Double.toString(a));
                        if(p1==null){
                            System.out.println("No such product id");
                        }
                        else {
                            System.out.print("Enter second product id: ");
                            double b = scanner.nextDouble();
                            scanner.nextLine();
                            product p2=products.get(Double.toString(b));
                            if(p2==null){
                                System.out.println("No such product id");
                            }
                            else {
                                System.out.println();
                                System.out.println("Enter combined price(normal prime elite) (should be less than ("+(p1.getprice()*(100-p1.getndiscount())/100+(p2.getprice()*(100-p2.getndiscount())/100))+" "+(p1.getprice()*(100-p1.getpdiscount())/100+(p2.getprice()*(100-p2.getpdiscount())/100))+" "+(p1.getprice()*(100-p1.getediscount())/100+(p2.getprice()*(100-p2.getediscount())/100))+"): ");
                                double n = scanner.nextDouble();
                                double p = scanner.nextDouble();
                                double e = scanner.nextDouble();
                                scanner.nextLine();
                                deal d = new deal(id,p1,p2,n,p,e);
                                deallist.put(id,d);
                            }
                        }
                    }
                    else if (x == 7) {
                        break;
                    }
                }
            }
            else{
                System.out.println("Invalid password");
            }
        }
        else{
            System.out.println("No such admin exists");
        }
    }
    public static void customer_menu(){
        Random r=new Random();
        while(true) {
            System.out.println("1) Sign Up");
            System.out.println("2) Log in");
            System.out.println("3) Back");
            System.out.print("\nEnter choice: ");
            int x = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            if (x == 1) {
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                if(customers.get(name)!=null){
                    System.out.println("User already exists");
                }
                else {
                    System.out.print("Enter password: ");
                    String pass = scanner.nextLine();
                    customer c = new customer(name, pass);
                    customers.put(name, c);
                    System.out.println("Customer successfully registered");
                }
            }
            else if (x == 2) {
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter password: ");
                String pass = scanner.nextLine();
                customer c = customers.get(name);
                if (c == null) {
                    System.out.println("No such customer exists");
                } else if (c.getpass().equals(pass)==false) {
                    System.out.println("Invalid password");
                } else {
                    while (true) {
                        System.out.println("Welcome " + name);
                        System.out.println("1) Browse products");
                        System.out.println("2) Browse deals");
                        System.out.println("3) add a product to cart");
                        System.out.println("4) add products in deal to cart");
                        System.out.println("5) view coupons");
                        System.out.println("6) check account balance");
                        System.out.println("7) view cart");
                        System.out.println("8) empty cart");
                        System.out.println("9) checkout cart");
                        System.out.println("10) upgrade customer status");
                        System.out.println("11) Add amount to wallet");
                        System.out.println("12) Back\n");
                        System.out.print("Enter choice: ");
                        int y = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println();

                        if (y == 1) {
                            displayproducts();
                        }

                        else if (y == 2) {
                            if(deallist.isEmpty()){
                                System.out.println("No deals available");
                            }
                            else {
                                for (deal d : deallist.values()) {
                                    d.getdetails(c,1);
                                }
                            }
                        }
                        else if (y == 3) {
                            System.out.print("Enter product id: ");
                            String id=scanner.nextLine();
                            product p=products.get(id);
                            if(p==null){
                                System.out.println("No such product id");
                            }
                            else{
                            System.out.print("Enter qty: ");
                            int qty=scanner.nextInt();
                            scanner.nextLine();
                            if(qty>p.getqty()){
                                System.out.println("Quantity should be less than "+p.getqty());
                            }
                            else {
                                c.addproduct(p,qty);
                                System.out.println("product added to cart\n");
                            }
                        }
                        }
                        else if (y == 4) {
                            System.out.print("Enter deal id: ");
                            String id=scanner.nextLine();
                            deal d=deallist.get(id);
                            if(d==null){
                                System.out.println("No such deal id");
                            }
                            else{
                            if(d.getp1().getqty()>0 && d.getp2().getqty()>0){
                                c.adddeal(d);
                            }
                        }
                        }
                        else if (y == 5) {
                            Object[] f=c.getcoupons().toArray();
                            if(f.length>0){
                            for(int i=0;i<c.getcoupons().size();i++){
                                System.out.println("coupon: "+f[i]);
                            }
                        }
                        else{
                            System.out.println("No coupons available");
                        }
                        }
                        else if (y == 6) {
                            System.out.println("balance: "+c.getbalance());
                        }
                        else if (y == 7) {
                            c.displaycart();
                        }
                        else if (y == 8) {
                            c.emptycart();
                            System.out.println("Cart emptied");
                        }
                        else if (y == 9) {
                            System.out.println("Proceeding to checkout.Details:");

                            if(c.getcart().isEmpty()==false){

                            if(c.getstatus()=="Normal"){

                                double price=c.ncheckout(products);
                                if(price==-1){
                                    System.out.println("please edit the cart");
                                }
                                else if(price>c.getbalance()){
                                    System.out.println("Insufficient balance");
                                }
                                else{
                                    c.displaycart();
                                    c.deductbalance(price + (5 * price / 100)+100);
                                    c.emptycart();
                                    System.out.println("Price: "+price);
                                    System.out.println("Delivery charges: "+(100+(5*price/100)));
                                    System.out.println("Total price: "+(price+(5*price/100)+100));
                                    System.out.println("Order placed.It will be delivered in "+r.nextInt(7,11)+" days");
                                }
                            }
                            else if(c.getstatus()=="Prime"){

                                double price=c.pcheckout(products);
                                if(price==-1){
                                    System.out.println("please edit the cart");
                                }
                                else if(price>c.getbalance()){
                                    System.out.println("Insufficient balance");
                                }
                                else {
                                    c.displaycart();
                                    c.deductbalance(price + (2 * price / 100)+100);
                                    c.emptycart();
                                    System.out.println("Price: " + price);
                                    System.out.println("Delivery charges: " + (100 + (2 * price / 100)));
                                    System.out.println("Total price: " + (price + (2 * price / 100)+100));
                                    System.out.println("Order placed.It will be delivered in " + r.nextInt(3, 7) + " days");
                                }
                            }
                            else if(c.getstatus()=="Elite") {
                                double price = c.echeckout(products);
                                if(price==-1){
                                    System.out.println("please edit the cart");
                                }
                                else if (price > c.getbalance()) {
                                    System.out.println("Insufficient balance");
                                } else {
                                    while (true) {
                                        product[] prods = products.values().toArray(new product[0]);
                                        int g = r.nextInt(0, prods.length);
                                        if (prods[g].getqty() > 0) {
                                            c.addproduct(prods[g], 1);
                                            System.out.println("Surprise product added");
                                            break;
                                        }
                                    }
                                    c.displaycart();
                                    c.deductbalance(price+100);
                                    c.emptycart();
                                    System.out.println("Price: " + price);
                                    System.out.println("Delivery charges: " + 100);
                                    System.out.println("Total price: " + (price + 100));
                                    System.out.println("Order placed.It will be delivered in " + r.nextInt(0, 3) + " days");
                                }
                            }
                            }
                            else{
                                System.out.println("Cart empty");
                            }

                        }
                        else if (y == 10) {
                            c.upgradestatus();
                        }
                        else if (y == 11) {
                            c.addamount();
                        }
                        else if (y == 12) {
                            break;
                        }
                        System.out.println();
                    }
                }
            } else if (x == 3) {
                break;
            }
        }
    }
}


