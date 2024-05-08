import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "./CategoryList.css";

function CategoryList() {
  const [categories, setCategories] = useState([]);
  const url = process.env.REACT_APP_API_URL;

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(url + "/api/v1/category/depth", {
          params: { depth: 0, parentId: null },
        });
        setCategories(response.data);
        console.log(categories);
      } catch (error) {
        console.error("Failed to fetch categories", error);
      }
    };

    fetchCategories();
  }, [url]); // 의존성 배열에 url 추가

  return (
    <div className="category-container">
      <div className="category-items">
        {categories.map((category, index) => (
          <Link
            key={index}
            to={`/products/${category.id}`}
            className="category-item"
          >
            {category.name}
          </Link>
        ))}
      </div>
    </div>
  );
}

export default CategoryList;
