import React, { useState } from "react";
import axios from "axios";
import {
  LockFill,
  EnvelopeFill,
  PersonFill,
  TelephoneFill,
  SignpostFill,
} from "react-bootstrap-icons";
import "./RegisterForm.css";

function RegisterForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [address, setAddress] = useState("");
  const url = process.env.REACT_APP_API_URL;
  const registerSubmit = () => {
    const userData = {
      email,
      password,
      name,
      phoneNumber,
      address,
    };

    axios
      .post(`${url}/member/create`, userData)
      .then((response) => {
        console.log("Success:", response.data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };

  return (
    <div className="register-form-section">
      <h1 className="register-title">회원가입</h1>

      {/* 이메일 */}
      <div className="input-section">
        <EnvelopeFill className="emoji" size={25} />
        <input
          type="text"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="이메일 입력하세요."
        />
      </div>

      {/* 비밀번호 */}
      <div className="input-section">
        <LockFill className="emoji" size={28} />
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="비밀번호를 입력하세요."
        />
      </div>

      {/* 이름 */}
      <div className="input-section">
        <PersonFill className="emoji" size={30} />
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="이름을 입력하세요."
        />
      </div>

      <div className="sex-check">
        <input
          type="radio"
          className="sex_select"
          name="sex"
          id="man"
          value="m"
        />

        <input
          type="radio"
          className="sex_select"
          name="sex"
          id="girl"
          value="f"
        />
      </div>

      {/* 전화번호 */}
      <div className="input-section">
        <TelephoneFill className="emoji" size={24} />
        <input
          type="text"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
          placeholder="전화번호를 입력하세요."
        />
      </div>

      {/* 주소 */}
      <div className="input-section">
        <SignpostFill className="emoji" size={25} />
        <input
          type="text"
          value={address}
          onChange={(e) => setAddress(e.target.value)}
          placeholder="주소를 입력하세요."
        />
      </div>

      {/* 회원가입 버튼 */}
      <div onClick={registerSubmit} className="r-register-button-section">
        <span>회원가입</span>
      </div>
    </div>
  );
}

export default RegisterForm;
