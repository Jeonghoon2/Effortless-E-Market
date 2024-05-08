import "./LatestProducts.css";
import React, { useState, useEffect } from "react";
import axios from "axios";

function LatestProducts() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const url = process.env.REACT_APP_API_URL;
  function getTimeElapsed(serverTime) {
    const currentTime = new Date();
    const pastTime = new Date(serverTime);
    const differenceInMilliseconds = currentTime - pastTime;
    const minutesElapsed = Math.floor(differenceInMilliseconds / 60000);
    const hoursElapsed = Math.floor(minutesElapsed / 60);

    if (hoursElapsed > 1) {
      return `${hoursElapsed}시간 전`;
    } else if (minutesElapsed > 60) {
      return `${hoursElapsed}시간 전`;
    } else if (minutesElapsed > 0) {
      return `${minutesElapsed}분 전`;
    } else {
      return `방금 전`;
    }
  }

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get(`${url}/api/v1/product/recent`);
        setItems(response.data.content);
        setLoading(false);
      } catch (error) {
        console.error("Failed to fetch latest products", error);
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="box3-container">
      <h1>최신 상품</h1>
      <div className="latest-product-wrap">
        {items.map((item, index) => (
          <a key={index} className="latest-product-card" href="/">
            <div className="latest-product-img">
              <img
                src={`${url}/api/v1/file?filepath=${item.thumbnail}`}
                alt=""
              />
            </div>
            <div className="latest-product-desc-container">
              <p className="latest-product-title">{item.name}</p>
              <p className="latest-product-desc">{item.description}</p>
              <div className="product-desc-2">
                <p>{item.price.toLocaleString()}원</p>
                <p className="time-elapsed">{getTimeElapsed(item.createdAt)}</p>
              </div>
            </div>
          </a>
        ))}
      </div>
    </div>
  );
}

export default LatestProducts;
