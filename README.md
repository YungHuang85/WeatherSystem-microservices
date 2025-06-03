台北天氣預報 - 拆成三組微服務架構

天氣分析微服務架構
1. weather-data-service (天氣資料服務)
   
  ※職責: 專門負責外部天氣 API 的呼叫與資料處理
  
    ．提供 REST API 給其他服務呼叫
    ．處理外部 API 呼叫邏輯
    ．資料轉換與驗證

2. weather-analysis-service (天氣分析服務)

  ※職責: 核心業務邏輯處理

    ．天氣資料分析與統計
    ．報告生成邏輯
    ．業務規則處理

3. notification-service (通知服務)

  ※職責: 負責各種通知管道的發送

    ．Teams 通知
    ．可擴展其他通知管道 (Email, Slack 等)
    ．通知模板管理

服務間通訊
    ．使用 REST API 進行同步通訊
    ．考慮使用 OpenFeign 簡化服務間呼叫
    ．每個服務獨立部署與擴展
