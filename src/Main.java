import exception.FindException;
import java.util.Scanner;

public class Main {
    final static private int PAGE_SIZE = 5;
     static private String userId = "";
    public static void main(String[] args) {
        initService();
    }

    /**
     * 사용자가 선택할 메뉴 출력
     * @param menu 메뉴 가변인자
     */
    public static void printMenu(String ...menu) {
        for (int i = 0; i < menu.length; i++) {
            System.out.println(String.format("%d. %s", i+1, menu[i]));
        }
    }
    /

    /**
     * 사용자가 선택한 메뉴 출력
     * @param menu 인덱스가 포함된 메뉴 전체 이름
     */
    public static void printDivide(String menu) {
        final int DIVIDE_REPEAT_COUNT = 100;
        if(menu == null) {
            System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
        } else {
            int remainDivide = DIVIDE_REPEAT_COUNT - menu.length() - 2;
            System.out.print("=".repeat(remainDivide / 2));
            System.out.print(String.format(" %s ", menu));
            System.out.print("=".repeat(remainDivide / 2));
        }
    }

    /**
     * 서비스 실행 첫 화면
     */
    public static void initService() {
        System.out.println("KOSA 플레이트에 방문해주셔서 감사합니다. 무엇을 도와드릴까요?");
        System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
        String userChoice = "-1";

        Scanner sc = new Scanner(System.in);
        if (userId.equals("")) {
            while(!userChoice.equals("5")) {
                printMenu("식당 검색하기", "식당 추천", "로그인", "회원 가입", "종료하기");

                System.out.print("번호를 입력해주세요: ");
                userChoice = sc.nextLine();

                switch (userChoice) {
                    case "1":
                        searchRestaurant();
                        break;
                    case "2":
                        recommendRestaurant();
                        break;
                    case "3":
                        login();
                        break;
                    case "4":
                        signup();
                        break;
                    case "5":
                        System.out.println("KOSA 플레이트를 종료합니다.");
                        break;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }
        } else {
            while(!userChoice.equals("4")) {
                printMenu("식당 검색하기", "식당 추천", "내 정보 보기", "종료하기");

                System.out.print("번호를 입력해주세요: ");
                userChoice = sc.nextLine();

                switch (userChoice) {
                    case "1":
                        searchRestaurant();
                        break;
                    case "2":
                        recommendRestaurant();
                        break;
                    case "3":
                        viewMyInfo();
                        break;
                    case "4":
                        System.out.println("KOSA 플레이트를 종료합니다.");
                        break;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }
        }

        printDivide(null);
        System.out.println("감사합니다. 좋은 하루 보내세요^^");
        sc.close();
    }

    /**
     * 검색 화면 출력
     */
    public static void searchRestaurant() {
        printDivide(null);
        Scanner sc = new Scanner(System.in);
        System.out.println("검색할 지역, 식당 또는 메뉴를 입력해주세요:");

//        String keyword = sc.nextLine();
//        RestaurantService rService = new RestaurantService();
//        rService.searchRestaurants(keyword, 1);
//        int totalResultCount = rService.getRestaurantCount();
        sc.close();
    }

    public static void recommendRestaurant() {
        System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
        String userChoice = "-1";
        Scanner sc = new Scanner(System.in);
        System.out.println("번호를 입력해주세요:");
        while(!userChoice.equals("4")) {
            printMenu("메뉴별 인기 맛집", "지역별 인기 맛집", "오늘 뭐먹지?", "이전으로 돌아가기");

            System.out.print("번호를 입력해주세요: ");
            userChoice = sc.nextLine();

            switch (userChoice) {
                case "1":
                    System.out.println("1");
                    break;
                case "2":
                    System.out.println("2");
                    break;
                case "3":
                    System.out.println("3");
                    break;
                case "4":
                    System.out.println("4");
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
        sc.close();
    }
    public static void login() {
        System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
    }
    public static void signup() {
        System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.print("아이디를 입력하세요: ");
            sc.nextLine();
            System.out.print("비밀번호를 입력하세요: ");
            sc.nextLine();
            System.out.print("이름을 입력하세요: ");
            sc.nextLine();
            System.out.print("성별을 입력하세요: ");
            sc.nextLine();
            System.out.print("주소(시/도 시/군/구 동/읍/면)를 입력하세요 (ex. 서울시 송파구 가락동): ");
            sc.nextLine();
            break;
        }

        sc.close();
    }
    public static void viewMyInfo() {
        System.out.println("=".repeat(DIVIDE_REPEAT_COUNT));
        String userChoice = "-1";
        Scanner sc = new Scanner(System.in);
        System.out.println("번호를 입력해주세요:");

        // UesrDAO uDAO = new UesrDAO();

        while(!userChoice.equals("5")) {
            printMenu("나의 찜 목록", "내가 쓴 리뷰", "비밀번호 수정", "회원 탈퇴", "이전으로 돌아가기");

            System.out.print("번호를 입력해주세요: ");
            userChoice = sc.nextLine();

            switch (userChoice) {
                case "1":
                    System.out.println("1");
                    break;
                case "2":
                    System.out.println("2");
                    break;
                case "3":
                    System.out.println("3");
                    break;
                case "4":
                    System.out.println("4");
                    break;
                case "5":
                    System.out.println("5");
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
        sc.close();
    }


}