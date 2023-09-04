## Mangoplate clone

### Introduce
- 검색을 통해 다양한 식당에 대해 정보와 리뷰를 제공하는 서비스
- 식당을 다양한 기준으로 분류해서 사용자에게 맛집을 추천하는 서비스
### Tech Stack
- Java
- Oracle DB
### How to start
1. Oracle Cloud의 전자지갑 파일이 필요합니다. 팀장에게 문의하세요.
2. 아래의 외부 `jar` 파일을 [여기](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html)에서 다운로드 받아 프로젝트에 포함시켜야 합니다. 사용 중인 오라클 DB의 버전은 19입니다.
   - `lombok.jar`
   - `ojdbc8.jar`
   - `oraclepki.jar`
   - `osdt_cert.jar`
   - `osdt_core.jar`
3. `application.properties` 파일을 `src` 폴더에 작성해야 합니다. 해당 파일에 아래의 내용이 포함되어야 합니다.
   - `jdbc.url`=오라클 클라우드 전자지갑 압축을 해제한 폴더 위치
   - `jdbc.user`=오라클 클라우드 계정 이름
   - `jdbc.password`=오라클 클라우드 계정 비밀번호


### ER Diagram
![erd](https://github.com/kosa-project1-team4/kosa-mango/assets/19462794/7c81e115-b797-4fb9-a557-f1146d9d820c)

### Class Diagram
![uml](https://github.com/kosa-project1-team4/kosa-mango/assets/19462794/854f60b5-86ce-4e5a-ab61-9cf44b43ffda)
