- `ソケット通信`とは、異なる機器やアプリケーションが、相互に通信を行うための仕組みで、`OSI 参照モデル`でいうと`トランスポート層`に位置付けられているプロトコル。
- `HTTP` や`メール`などの通信は、`OSI 参照モデル`の`アプリケーション層`で行われている。

|  層  |  名称  | 主な役割 | 領域 | 関連装置
| ---- | ---- | --- | --- | --- |
|  L7  |  アプリケーション層  |  アプリケーション間でのやり取り   | ソフトウェア  | ゲートウェイ  
|  L6  |  プレゼンテーション層  |  データの表現形式を定義   |   | 
|  L5  |  セッション層  | 接続の手順    |   | 
|  L4  |  トランスポート層  | データ通信の制御   |   |   
|  L3  |  ネットワーク層 | インターネットでの通信 |  |
|  L2  |  データリンク層 | 同一ネットワークでの通信 | |
|  L1  |  物理層 | ケーブルや電気信号やコネクタど | ハードウエア | LANケーブル、NIC