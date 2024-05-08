import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./ProductList.css";

function ProductList() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  const [childCategories, setChildCategories] = useState([]);
  const [selectedChildCategoryId, setSelectedChildCategoryId] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const url = process.env.REACT_APP_API_URL;
  const { categoryId } = useParams();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(
          `${url}/api/v1/category/child/${categoryId}`
        );
        setCategories(response.data);
        setChildCategories([]);
      } catch (error) {
        console.error("Failed to fetch categories", error);
      }
    };

    fetchCategories();
  }, [url, categoryId]);

  const handleCategoryClick = async (categoryId) => {
    try {
      const response = await axios.get(
        `${url}/api/v1/category/child/${categoryId}`
      );
      setSelectedCategoryId(categoryId);
      setTotalElements(0);
      setChildCategories(response.data);
      setProducts([]);
    } catch (error) {
      console.error("Failed to fetch child categories", error);
    }
  };

  const handleChildCategoryClick = async (childCategoryId) => {
    try {
      setSelectedChildCategoryId(childCategoryId);
      const response = await axios.get(
        `${url}/api/v1/product/category/${childCategoryId}`
      );
      setSelectedCategoryId(childCategoryId);
      setProducts(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(response.data.totalElements);
    } catch (error) {
      console.error("Failed to fetch products for child category", error);
    }
  };

  const handlePrevClick = () => {
    if (selectedChildCategoryId) {
      setCurrentPage((prev) => Math.max(prev - 1, 1));
      handleChildCategoryClick(selectedChildCategoryId);
    }
  };

  const handleNextClick = () => {
    if (selectedChildCategoryId) {
      setCurrentPage((prev) => Math.min(prev + 1, totalPages));
      handleChildCategoryClick(selectedChildCategoryId);
    }
  };

  return (
    <div className="product-list-section">
      <div className="product-list-nav-list">
        <div className="product-list-nav-items">
          {categories.map((category) => (
            <div
              key={category.id}
              onClick={() => handleCategoryClick(category.id)}
              className={`product-list-nav-item ${
                selectedCategoryId === category.id ? "active" : ""
              }`}
            >
              {category.name}
            </div>
          ))}
        </div>
      </div>
      {childCategories.length > 0 && (
        <div className="product-list-nav-list l2">
          <div className="product-list-nav-items">
            {childCategories.map((child) => (
              <div
                key={child.id}
                onClick={() => handleChildCategoryClick(child.id)}
                className={`product-list-nav-item ${
                  selectedCategoryId === child.id ? "active" : ""
                }`}
              >
                {child.name}
              </div>
            ))}
          </div>
        </div>
      )}

      {products.length > 0 && (
        <>
          <div className="total-count">
            <div>총 : {totalElements}</div>
          </div>
          <div className="hr"></div>
        </>
      )}
      <div className="product-card-list">
        {products.length > 0 ? (
          products.map((product) => (
            <a
              key={product.id}
              className="product-card"
              href={`/product/${product.id}`}
            >
              <div className="product-img">
                <img
                  src={`${url}/api/v1/file?filepath=${product.thumbnail}`}
                  alt="Product"
                  className="product-img-p"
                />
              </div>
              <div className="product-list-card-desc">
                <p className="product-list-card-title">{product.name}</p>
                <p className="product-list-card-description">
                  설명 : {product.description}
                </p>
                <p className="product-list-card-price">
                  가격 : {product.price.toLocaleString("ko-KR")}원
                </p>
              </div>
              <div className="product-link-button">상세 보기</div>
            </a>
          ))
        ) : (
          <p className="info">카테고리를 선택해주세요...</p>
        )}
      </div>

      <div className="pagination-section">
        {currentPage > 1 && (
          <button onClick={handlePrevClick} className="page-prev-btn">
            이전
          </button>
        )}
        {totalElements > 0 && (
          <span>
            Page {currentPage} of {totalPages}
          </span>
        )}

        {currentPage < totalPages && (
          <button onClick={handleNextClick}>다음</button>
        )}
      </div>
    </div>
  );
}

export default ProductList;
