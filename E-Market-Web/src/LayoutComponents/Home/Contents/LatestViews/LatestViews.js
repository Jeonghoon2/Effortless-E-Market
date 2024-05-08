import "./LatestViews.css";

function LatestViews() {
  const items = [
    {
      price: "50,000",
      title: "블루투스 헤드폰",
      desc: "특수한 소리가 들리는 블루투스 절찬 특가",
      timeElapsed: 1,
    },
    {
      price: "30,000",
      title: "휴대용 충전기",
      desc: "고속 충전이 가능한 휴대용 충전기, 어디서나 빠르게",
      timeElapsed: 3,
    },
    {
      price: "120,000",
      title: "스마트 워치",
      desc: "건강 관리부터 일정 관리까지, 스마트한 생활을 위한 선택",
      timeElapsed: 6,
    },
    {
      price: "200,000",
      title: "노이즈 캔슬링 이어폰",
      desc: "외부 소음을 완벽하게 차단, 최상의 음질을 경험하세요",
      timeElapsed: 6,
    },
  ];

  return (
    <div className="latest-views-container">
      <h1>최근 본 상품</h1>
      <div className="latest-views-wrap">
        {items.map((item, index) => (
          <a key={index} className="latest-views-card" href="/">
            <div className="latest-views-img"></div>

            <div className="latest-views-desc-container">
              <p className="latest-product-title">{item.title}</p>
              <p className="latest-product-desc">{item.desc}</p>
              <p>{item.price}원</p>
            </div>
          </a>
        ))}
      </div>
    </div>
  );
}

export default LatestViews;
