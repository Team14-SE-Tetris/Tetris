# Tetris
SE Lec TeamProject



# 중간 평가 명세서

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/69c16400-b496-43b5-ace6-5e721a3c5b54/39bb9de3-a213-45ff-b242-ac9c78e09eab/Untitled.png)

블럭 하나 형태의 아이템 입니다.

이 블럭이 포함된 상태에서 줄의 형태로 제거된다면 (아이템에 의한 줄 제거 포함) 100점을 추가로 획득합니다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/69c16400-b496-43b5-ace6-5e721a3c5b54/1e7db9e0-3418-41ea-859b-2cf4670992d6/Untitled.png)

이 아이템은 3개 고정된 형태의 아이템 입니다.

이 아이템은 3개 중 하나가 랜덤으로 줄 삭제를 수행합니다.

여기서의 줄 삭제는 실제 줄 삭제와 같이 점수를 얻습니다.

줄 삭제가 실행되면 이 아이템은 사라집니다.

사용자는 어디에 아이템의 줄 제거인지 사용되기 전에 확인할 수 없습니다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/69c16400-b496-43b5-ace6-5e721a3c5b54/fa9ec96f-dba7-426e-b343-91a1f8826c9f/Untitled.png)

이 아이템은 위와 같은 고정된 형태로 존재합니다.

블럭들이 쌓여있는곳에 쌓이면 이 아이템 주변1칸의 블럭은 모두 사라집니다.

이 아이템 역시 사라집니다.

여기서 사라진것은 점수와 관련이 되지 않습니다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/69c16400-b496-43b5-ace6-5e721a3c5b54/ee752611-501b-420a-9280-4ac1c07e6a26/Untitled.png)

추가적인 점수 증가 방식으로는 deleteBar가 상승할때마다 점수가 100점씩 증가합니다.

deleteBar는 가로 한 줄이 삭제될때 마다 증가합니다.

줄 삭제 아이템들의 작용으로도 증가합니다.
