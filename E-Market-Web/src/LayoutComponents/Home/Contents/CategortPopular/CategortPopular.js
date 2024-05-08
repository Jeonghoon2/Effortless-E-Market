import "./CategortPopular.css";
import { ChevronLeft, ChevronRight } from "react-bootstrap-icons";
import React, { useState } from "react";
import noImage from "./image/no-image.png";

function CategortPopular() {
  const items = [
    {
      price: "50,000",
      title: "블루투스 헤드폰",
      desc: "특수한 소리가 들리는 블루투스 절찬 특가",
      image: { noImage },
    },
    {
      price: "30,000",
      title: "휴대용 충전기",
      desc: "고속 충전이 가능한 휴대용 충전기, 어디서나 빠르게",
      image: { noImage },
    },
    {
      price: "120,000",
      title: "스마트 워치",
      desc: "건강 관리부터 일정 관리까지, 스마트한 생활을 위한 선택",
      image: { noImage },
    },
    {
      price: "200,000",
      title: "노이즈 캔슬링 이어폰",
      desc: "외부 소음을 완벽하게 차단, 최상의 음질을 경험하세요",
      image: { noImage },
    },
    {
      price: "1,500,000",
      title: "고성능 노트북",
      desc: "강력한 성능, 휴대성을 겸비한 프리미엄 노트북",
      image: { noImage },
    },
    {
      price: "600,000",
      title: "프로 게이밍 마우스",
      desc: "정확한 컨트롤을 위한 게이머 필수 아이템",
      image: { noImage },
    },
    {
      price: "90,000",
      title: "와이어리스 충전 패드",
      desc: "편리한 사용을 위한 무선 충전, 깔끔한 책상을 유지하세요",
      image: { noImage },
    },
    {
      price: "40,000",
      title: "LED 데스크 램프",
      desc: "눈의 피로를 줄여주는 조절 가능한 밝기",
      image: { noImage },
    },
    {
      price: "70,000",
      title: "스마트폰 거치대",
      desc: "다양한 각도 조절이 가능하여 편리한 스마트폰 사용을 도와줍니다",
      image: { noImage },
    },
    {
      price: "15,000",
      title: "USB 메모리 스틱",
      desc: "대용량 파일 저장이 필요할 때 간편하게 사용 가능한 USB 메모리 스틱",
      image: { noImage },
    },
    {
      price: "90,000",
      title: "와이어리스 충전 패드",
      desc: "편리한 사용을 위한 무선 충전, 깔끔한 책상을 유지하세요",
      image: { noImage },
    },
    {
      price: "40,000",
      title: "LED 데스크 램프",
      desc: "눈의 피로를 줄여주는 조절 가능한 밝기",
      image: { noImage },
    },
    {
      price: "70,000",
      title: "스마트폰 거치대",
      desc: "다양한 각도 조절이 가능하여 편리한 스마트폰 사용을 도와줍니다",
      image: { noImage },
    },
    {
      price: "15,000",
      title: "USB 메모리 스틱",
      desc: "대용량 파일 저장이 필요할 때 간편하게 사용 가능한 USB 메모리 스틱",
      image: { noImage },
    },
  ];

  const categories = ["남성의류", "여성의류", "가전제품"];

  const itemsPerPage = 10;
  const [activeCard, setActiveCard] = useState(categories[0]);
  const [currentPage, setCurrentPage] = useState(0);

  const handleCardClick = (category) => {
    setActiveCard(category);
  };

  const handleNextClick = () => {
    const totalPages = Math.ceil(items.length / itemsPerPage);
    setCurrentPage((prevPage) => (prevPage + 1) % totalPages);
  };

  const handlePrevClick = () => {
    const totalPages = Math.ceil(items.length / itemsPerPage);
    setCurrentPage((prevPage) => (prevPage - 1 + totalPages) % totalPages);
  };

  const currentItems = items.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  return (
    <div className="box2-container">
      <p className="box2-title">카테고리 별 인기순위</p>

      <div className="category-card-list">
        {categories.map((category, index) => (
          <button
            key={index}
            className={`category-card ${
              activeCard === category ? "card-active" : ""
            }`}
            onClick={() => handleCardClick(category)}
          >
            <p>{category}</p>
          </button>
        ))}
      </div>

      <div className="product-rank-list">
        {currentItems.map((item, index) => (
          <a className="product-rank-item" key={index} href="/">
            <p className="rank">{currentPage * itemsPerPage + index + 1}위</p>
            <img src={noImage} alt={item.title} />
            <p className="product-name">{item.title}</p>
            <p className="product-desc">{item.desc}</p>
            <p className="product-price">{item.price}원</p>
          </a>
        ))}
      </div>
      <div className="rank-slide-btn">
        <button onClick={handlePrevClick}>
          <ChevronLeft size={20} />
        </button>
        <button onClick={handleNextClick}>
          <ChevronRight size={20} />
        </button>
      </div>
    </div>
  );
}

export default CategortPopular;
