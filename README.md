## 구성 환경
- spring boot 3.3.1

- spring data jpa

-  thymeleaf

-  mysql8.3

-  spring data redis

-  h2 database(test DB)

-  spring security

-  spring OAuth2.0
  
-  spring mvc

-  jQuery 3.7.0

## 프로젝트 구조
<img src="https://github.com/user-attachments/assets/c8b578f4-50e5-41d4-a942-237a60fed841" style="width:900px">
</img>

## 로그인
### 로그인 5회 이상 실패
1. 로그인이 불가능 하다.
2. 계정이 잠겼다는 메일을 계정의 이메일로 보낸다.
<img src="https://github.com/user-attachments/assets/e71bcd70-5a55-4de9-9657-d0b1492a1783" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/2e445439-620c-4af2-8a1b-da5cadc7cacd" style="width:600px">
</img>


### 존재하지 않는 계정으로 로그인할 경우
<img src="https://github.com/user-attachments/assets/d535cc48-7079-49f9-b1b6-8268de9fe910" style="width:400px">
</img>

### 아이디나 비밀번호가 일치하지 않는 경우

<img src="https://github.com/user-attachments/assets/59f7bd39-95e0-47d0-9e7c-ee7d78cdc0b4" style="width:400px">
</img>

## 회원가입
### 회원가입 화면
<img src="https://github.com/user-attachments/assets/13758d39-7be0-4eb5-84be-6884313db12a" style="width:400px">
</img>

### 회원가입 부적절한 값 입력 화면

<img src="https://github.com/user-attachments/assets/c37c7007-2c8f-469b-bf61-2d79683ead49" style="width:400px">
</img>

### 회원가입 인증 요구, 인증 메일, 인증 성공 화면

<img src="https://github.com/user-attachments/assets/5d5b18a3-b404-4a8d-8339-c20c8a503175" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/4a36af9a-a77e-4196-863a-1e59217245ec" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/63b815fb-dd59-4060-8e71-8a1c259d3b16" style="width:400px">
</img>

## 아이디 찾기
1. 이름, 이메일 입력 후 인증 요청 버튼 클릭
2. 이메일로 인증 코드 확인 후 입력
3. 이름, 이메일과 해당하는 아이디 조회

<img src="https://github.com/user-attachments/assets/792cbf97-9be9-4b91-9962-37b1c22117fe" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/70235ae5-f7b5-4354-946b-6dff2ef2f453" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/c94935dc-91ed-4fba-9b33-16e0411bf756" style="width:400px">
</img>

## 비밀번호 찾기
포워딩 방식으로 페이지 화면을 전환한다.

서버로 요청하는 파라미터의 값에 따라 페이지가 다르다.

파라미터 menu값에 따라 다른 페이지를 보여준다.

1단계는 필요없으나 2단계 부터는 서버에서 token_help가 파라미터로 필요하다.

### 1단계 비밀번호 찾을 계정을 입력(menu = X, token_help필요 X)
<img src="https://github.com/user-attachments/assets/2449b6d6-6c53-491b-9aa6-7a09aa9a097c" style="width:400px">
</img>

### 2단계 비밀번호 인증 요청 및 인증 메일 수신(menu = viewPasswordAuth, token_help필요 O)

<img src="https://github.com/user-attachments/assets/3fed8791-3cbb-401d-a3eb-0e76eb871278" style="width:800px">
</img>

1. 이름, 이메일을 입력 후 인증 요청 클릭
2. 이메일로 인증 코드 확인 후 입력

<img src="https://github.com/user-attachments/assets/d4b57f0c-ae13-4a72-ac7e-f2f887b8ea18" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/b241d016-00e7-422d-8d7c-9be3bff42525" style="width:400px">
</img>



### 3단계 비밀 번호 재설정(menu = viewPasswordInput, token_help필요 O)
<img src="https://github.com/user-attachments/assets/f356f8e4-85d2-431c-9942-cac4382cbae2" style="width:800px">
</img>
1. 새 비밀번호와 비밀번호 확인 입력
2. 이전과 같은 비밀번호이거나 비밀번호 형식에 맞지 않을 경우 경고 메세지 발생

<img src="https://github.com/user-attachments/assets/0b718522-bafc-4a65-9083-27baa1c01c34" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/8a76ea03-ba61-4ba6-9177-3a1b359aa58d" style="width:400px">
</img>

<img src="https://github.com/user-attachments/assets/5f8b7dc9-af5b-4f47-90a4-6118712d7b4b" style="width:400px">
</img>

### 만약 비밀번호 찾기 세션이 만료된 경우

<img src="https://github.com/user-attachments/assets/b320077e-ffd7-45ea-b3a2-8ef66b274ceb" style="width:400px">
</img>







