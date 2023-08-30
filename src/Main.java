import exception.FindException;
import restaurant.service.RestaurantService;

import java.util.Scanner;

public class Main {
    final static private int PAGE_SIZE = 5;
     static private String userId = "";
    public static void main(String[] args) throws FindException {
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
            System.out.println("=".repeat(remainDivide / 2));
        }
    }

    /**
     * 서비스 실행 첫 화면
     */
    public static void initService() throws FindException {
        System.out.println("KOSA 플레이트에 방문해주셔서 감사합니다. 무엇을 도와드릴까요?");
        printDivide(null);
        String userChoice = "-1";

        Scanner sc = new Scanner(System.in);
        if (userId.equals("")) {
            while(!userChoice.equals("5")) {
                printMenu("식당 검색하기", "식당 추천", "로그인", "회원 가입", "종료하기");
                printDivide(null);

                System.out.print("번호를 입력해주세요: ");
                userChoice = sc.nextLine();

                switch (userChoice) {
                    case "1":
                        searchRestaurant("1.식당 검색하기");
                        break;
                    case "2":
                        recommendRestaurant("2. 식당 추천");
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

                System.out.print("번호를 입력하세요: ");
                userChoice = sc.nextLine();

                switch (userChoice) {
                    case "1":
                        searchRestaurant("1. 식당 검색하기");
                        break;
                    case "2":
                        recommendRestaurant("2. 식당 추천");
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
    public static void searchRestaurant(String menu) throws FindException {
        printDivide(menu);
        Scanner sc = new Scanner(System.in);
        System.out.print("검색할 지역, 식당 또는 메뉴를 입력해주세요: ");
        String keyword = sc.nextLine();
        System.out.println();
        RestaurantService rService = new RestaurantService(PAGE_SIZE);

        int index = 1;
        int userInput = 0;
        do  {
            int beforeIndex = Integer.MIN_VALUE;
            if (index != 1) {
                System.out.println("0. 이전으로\n");
                beforeIndex = 0;
            }
            rService.printRestaurantList("GENERAL_SEARCH", keyword, index);
            System.out.println();
            if (rService.getRestaurantCount() == 0) {
                System.out.println("검색 결과가 없습니다.");
                printDivide(null);
                break;
            }
            int totalPage = rService.getRestaurantCount() % PAGE_SIZE != 0? rService.getRestaurantCount() / PAGE_SIZE + 1: rService.getRestaurantCount() / PAGE_SIZE;
            int nextIndex = Integer.MIN_VALUE;;
            int quitIndex = Integer.MIN_VALUE;;

            if (index < totalPage) {
                nextIndex = PAGE_SIZE + 1;
                quitIndex = PAGE_SIZE + 2;
                System.out.println(String.format("%d. %s", nextIndex, "다음으로"));
                System.out.println(String.format("%d. %s", quitIndex, "종료하기"));
            } else if (index == totalPage) {
                if (rService.getRestaurantCount() % PAGE_SIZE == 0) {
                    quitIndex = PAGE_SIZE + 1;
                    System.out.println(String.format("%d. %s", PAGE_SIZE + 1, "종료하기"));
                } else {
                    quitIndex = rService.getRestaurantCount() % PAGE_SIZE + 1;
                    System.out.println(String.format("%d. %s", quitIndex, "종료하기"));
                }
            }
            printDivide("전체 검색 결과: " + rService.getRestaurantCount());
            System.out.print("번호를 입력하세요: ");
            userInput = Integer.parseInt(sc.nextLine());
            if (userInput == beforeIndex) {
                index--;
            } else if (userInput == nextIndex) {
                index++;
            } else if (userInput == quitIndex) {
                break;
            } else if (userInput <= PAGE_SIZE && userInput > 0) {
                viewDetailRestaurant(userInput);
            } else {
                System.out.println("잘못된 입력입니다. 처음 검색 결과로 돌아갑니다.");
                index = 1;
            }
        } while (true);
    }

    public static void viewDetailRestaurant(int restaurantIndex) throws FindException {
        RestaurantService rService = new RestaurantService(PAGE_SIZE);
        printDivide("식당 상세 정보");
        rService.printDetailRestaurant("VIEW_DETAIL", restaurantIndex);
        printDivide(null);
    }
    public static void recommendRestaurant(String menu) {
        printDivide(menu);
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
    }
    public static void login() {
        printDivide(null);
    }
    public static void signup() {
        printDivide(null);
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

    }
    public static void viewMyInfo() {
        printDivide(null);
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
    }


}