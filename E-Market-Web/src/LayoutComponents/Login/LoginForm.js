import { EyeSlashFill } from "react-bootstrap-icons";
import "./LoginForm.css";

function LoginForm() {
  return (
    <div className="login-form-section">
      <h1 className="login-title">로그인</h1>

      {/* 로그인 */}
      <div className="input-section">
        <input type="text" placeholder="아이디를 입력하세요." />
      </div>

      {/* 패스워드 */}
      <div className="input-section">
        <input type="password" placeholder="비밀번호를 입력하세요." />
        <EyeSlashFill className="password-eye" size={20} color="#979797" />
      </div>

      {/* 체크 박스 */}

      <div className="check-section">
        <div className="check-control">
          <div className="check">
            <input type="checkbox" id="check"></input>
            <label for="check"></label>
          </div>
        </div>
        <span>로그인 상태 유지</span>
        <a href="/find">아이디/비밀번호 찾기</a>
      </div>

      {/* 로그인 버튼 */}
      <div className="login-button-section">
        <span>로그인</span>
      </div>

      {/* 회원가입 버튼 */}
      <a href="/register">
        <div className="register-button-section">
          <span>회원가입</span>
        </div>
      </a>
    </div>
  );
}

export default LoginForm;
