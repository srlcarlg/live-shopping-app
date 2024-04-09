import productImg from "../../assets/product-1.png";

type Props = {};

const ProductCount = (props: Props) => {
  return (
    <div className="product-count-container">
      <img src={productImg} alt="" />
      <div className="product-count-info">
        <div className="text-column">
          The great headset Stereo/5.1/7.1
          <span className="price">$30.14</span>
        </div>
        <div className="button-column">
          <div className="button-row">
            <button className="btn btn-less">-</button>
            <span>2</span>
            <button className="btn btn-more">+</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductCount;
