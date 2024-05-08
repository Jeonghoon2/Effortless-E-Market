import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./ProductDetail.css";

function ProductDetail() {
  const { productId } = useParams();
  const url = process.env.REACT_APP_API_URL;
  const [product, setProduct] = useState({
    id: "",
    name: "",
    description: "",
    quantity: "",
    price: 0,
    sellerName: "",
    categoryName: "",
    thumbnail: "",
    createdAt: "",
  });

  const navigate = useNavigate();

  const goBack = () => {
    navigate(-1);
  };

  useEffect(() => {
    axios
      .get(`${url}/api/v1/product/${productId}`)
      .then((response) => {
        const {
          id,
          name,
          description,
          quantity,
          price,
          sellerName,
          brandName,
          categoryName,
          thumbnail,
          createdAt,
        } = response.data;
        setProduct({
          id,
          name,
          description,
          quantity,
          price,
          sellerName,
          brandName,
          categoryName,
          thumbnail,
          createdAt,
        });
      })
      .catch((error) => {
        console.error("Error fetching product details:", error);
      });
  }, [productId]);

  return (
    <div className="product-section">
      <div className="product-container">
        <div className="product-container-sub">
          <div className="product-img-section">
            <img
              src={`${url}/api/v1/file?filepath=${product.thumbnail}`}
              alt={product.title}
            />
          </div>
          <div className="product-desc-section">
            <div className="product-title">{product.name}</div>
            <div className="product-description">{product.description}</div>
            <div className="propduct-seller-name">
              <strong>판매자</strong>: {product.sellerName}
            </div>
            <div className="propduct-brand-name">
              <strong>스토어</strong>: {product.brandName}
            </div>
            <div className="propduct-category">{product.categoryName}</div>
            <div className="propduct-price">
              {product.price.toLocaleString()}원
            </div>
            <div className="product-buy-btn-section">
              <div className="product-buy-cnt-section">
                <input type="text" value={1} readOnly></input>
              </div>
              <div className="product-buy-btn">구매</div>
            </div>
          </div>
        </div>
        <div className="thumbnail-slide">
          <div className="thubnail-slide-box"></div>
          <div className="thubnail-slide-box"></div>
          <div className="thubnail-slide-box"></div>
        </div>
      </div>
      <div className="back-button" onClick={goBack}>
        목록으로
      </div>
    </div>
  );
}

export default ProductDetail;
