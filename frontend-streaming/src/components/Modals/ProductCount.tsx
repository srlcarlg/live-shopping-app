import React from "react";
import { ProductResponse } from "../../api/store/models";
import productImg from "../../assets/product-1.png";

type Props = {
  productResponse?: ProductResponse;
  onValueChange: (newValue: number) => void;
};

const ProductCount = (props: Props) => {
  const { productResponse, onValueChange } = props;

  const [count, setCount] = React.useState<number>(1);
  const increment = () => {
    const newCount = count + 1;
    setCount(newCount);
    onValueChange(newCount);
  };
  const decrement = () => {
    if (count > 1) {
      const newCount = count - 1;
      setCount(newCount);
      onValueChange(newCount);
    }
  };

  return (
    <div className="product-count-container">
      <img src={productImg} alt="" />
      <div className="product-count-info">
        <div className="text-column">
          {productResponse?.title}
          <span className="price">${productResponse?.price! * count}</span>
        </div>
        <div className="button-column">
          <div className="button-row">
            <button className="btn btn-less" onClick={decrement}>
              -
            </button>
            <span>{count}</span>
            <button className="btn btn-more" onClick={increment}>
              +
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductCount;
