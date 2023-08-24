## 망고플레이트 클론

### 실행 방법
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