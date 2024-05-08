import './RecomandItem.css'

function RecomandItem({value}) {

    const items = [
        { price: "50,000원", title: "블루투스 헤드폰", image: "path/to/bluetooth_headphones.jpg" },
        { price: "15,000원", title: "고급 노트북 가방", image: "path/to/laptop_bag.jpg" },
        { price: "22,000원", title: "스마트폰 거치대", image: "path/to/phone_stand.jpg" },
        { price: "12,000원", title: "휴대용 USB 충전기", image: "path/to/portable_charger.jpg" },
        { price: "40,000원", title: "전자 북 리더", image: "path/to/ebook_reader.jpg" },
        { price: "35,000원", title: "무선 마우스", image: "path/to/wireless_mouse.jpg" }
      ];

    console.log(value)
    
    return (
        <div class="section">

                <div class="text_section"> 
                    <a href="/item" class='text_section2'>바로가기</a>
                    <p class='recommand_subject1'>Today Pick</p>
                    <span class='recommand_title'>{value} </span>
                    <span class='recommand_subject1'> 지금 확인해 보세요!</span>
                </div>
                
                <div class="items">
                {items.map((item,index)=>(
                    <a href='/product' key={index} class="product_section">
                        <div class="img_section"></div>
                        <div>{item.price}</div>
                        <div class="title_section">{item.title}</div>
                    </a>
                ))}
                </div>
                
            </div>
            

    );
}

export default RecomandItem;