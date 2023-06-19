package supports;

public class OwnerManagerJava {

        private static String owner = "unknown";
        public static void setOwner(String newowner){
            owner = newowner;
        }
        public static String getOwner(){
            return owner;
        }
        public static boolean checkOwner(String caller){
            return owner.equals(caller);
        }
}
