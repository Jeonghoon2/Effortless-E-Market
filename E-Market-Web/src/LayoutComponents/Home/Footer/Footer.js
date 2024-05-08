import './Footer.css'
function Footer() {
    return (

        <div className='footer_section'>
            <div className='midle_section'>
                <div className='button_section'>
                    <a href="/" className='f-button-1'> 회사 소개</a>
                    <div className='line2'/>
                    <a href="/" className='f-button-1'> 이용 약관</a>
                    <div className='line2'/>
                    <a href="/" className='f-button-1'> 개인정보처리방침</a>
                    <div className='line2'/>
                    <a href="/" className='f-button-1'> 임점/제휴문의</a>
                    <div className='line2'/>
                    <a href="/" className='f-button-1'> 대량 구매 문의</a>

                    <a href='/seller' className='f-button-2'> 판매자 서비스</a>
                </div>
            </div>

            <div className='line'/>

            <div className='midle_section'>
                <div className='first_section'>
                    <p className='l-title'>(주) Effort E-Market</p>
                    <p className='r-title'>고객센터 1555-9999</p>
                </div>
                
                <div className='secon_section'>
                    <p className='l-subject'>대표이사 : 이포트</p>
                    <div className='line2'/>
                    <p className='l-subject'>서울특별시 OO구 OO동 OO빌딩</p>
                    <div className='line2'/>
                    <p className='l-subject'>사업자등록번호 : 1111-22-3333</p>
                    <p className='r-subject'>운영시간 : 평일 09:00~18:00</p>

                </div>
                <div className='secon_section'>
                    <p className='l-subject'>이메일 : ukidd002@gmail.com</p>
                    
                    <a href='/seller'><p className='r-subject'>고객센터 바로가기</p></a>

                </div>
            </div>
        </div>
      
    );
  }
  
  export default Footer;
  