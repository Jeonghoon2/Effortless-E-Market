import "./Header.css";

import {
  Search,
  PersonFill,
  Bag,
  Heart,
  JustifyLeft,
} from "react-bootstrap-icons";

function Header() {
  return (
    <div className="nav-form">
      <nav className="nav-items-1-section">
        <div className="nav-items-1-container">
          <div className="nav-items-1">
            <a className="nav-item-1 nav-item-logo" href="/">
              Effort Market
            </a>
            <div className="nav-item-1 search-box">
              <input type="text" placeholder="이달의 상품" />
              <Search className="search-icon" size={20} />
            </div>
          </div>

          <div className="nav1-item-section">
            <a className="nav-item-1" href="/cart">
              {" "}
              <Bag color="#222F23" size={30} />
            </a>
            <a className="nav-item-1" href="/like">
              {" "}
              <Heart color="#222F23" size={30} />{" "}
            </a>
            <a className="nav-item-1" href="/login">
              {" "}
              <PersonFill color="#222F23" size={30} />
            </a>
          </div>
        </div>
      </nav>

      <nav className="nav-item-2-section">
        <div className="nav-items-2-container">
          <div className="nav-items-2">
            <button className="nav-item-2">
              <JustifyLeft color="#ffffff" size={20}></JustifyLeft>카테고리
            </button>
            <a className="nav-item-2" href="/">
              판매자 순위
            </a>
            <a className="nav-item-2" href="/">
              조회 순위
            </a>
          </div>

          <div className="nav-items-2">
            <a className="nav-item-2" href="/login">
              로그인
            </a>
            <a className="nav-item-2" href="/register">
              회원가입
            </a>
            <a className="nav-item-2" href="/">
              고객센터
            </a>
          </div>
        </div>
      </nav>
    </div>
  );
}

export default Header;
