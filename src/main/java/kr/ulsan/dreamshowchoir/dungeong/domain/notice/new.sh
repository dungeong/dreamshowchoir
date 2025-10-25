# 1단계: 파일 이름을 완전히 다른 임시 이름으로 변경합니다.
git mv "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post/post.java" "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post/Post_temp.java"

# 2단계: 임시 이름에서 최종적으로 원하는 이름(Post.java)으로 다시 변경합니다.
git mv "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post/Post_temp.java" "src/main/java/kr/ulsan/dreamshowchoir/dungeong/domain/post/Post.java"