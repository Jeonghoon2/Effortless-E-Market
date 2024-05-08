import "./Advertisement.css"
import Carousel from 'react-bootstrap/Carousel'


import sale from './tmp_img/clothe.jpg'
import bugger from './tmp_img/clothe2.jpg'
import water from './tmp_img/image3.jpg'

function Advertisement() {

    return (
        <Carousel fade>
            <Carousel.Item>
                <img src={sale} alt="sale"/>
                <Carousel.Caption>
                
                </Carousel.Caption>
            </Carousel.Item>
            <Carousel.Item>
            <img src={bugger} alt="bugger"/>
                <Carousel.Caption>
                
                </Carousel.Caption>
            </Carousel.Item>
            <Carousel.Item>
            <img src={water} alt="water"/>
                <Carousel.Caption>
                
                </Carousel.Caption>
            </Carousel.Item>
        </Carousel>

    );
}

export default Advertisement;