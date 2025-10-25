# 1. 패키지 디렉토리를 임시 이름(예: post_temp)으로 변경합니다.
# git mv [기존 폴더 경로] [임시 폴더 경로]
git mv "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post" "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post_temp"

# 2. 임시 이름을 최종적으로 원하는 이름(post)으로 다시 변경합니다.
# Java 패키지 명명 규칙은 소문자를 권장하므로 'post'로 변경하는 것을 추천합니다.
git mv "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post_temp" "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post"