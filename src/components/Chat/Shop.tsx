import React from "react";
import addIcon from "../../assets/add-icon.svg";
import Product from "./Product";

type Props = {};

const Shop = (props: Props) => {
  const [isBroadcaster, setIsBroadcaster] = React.useState(true);

  return (
    <div className="shop">
      <div className="shop-header">
        {isBroadcaster ? (
          <button className="btn btn-add">
            <div className="text">
              <img src={addIcon} alt="" />
              Add
            </div>
          </button>
        ) : (
          <div className="n-products">3 products</div>
        )}
      </div>
      <div className="products-container">
        <div className="grid">
          <Product />
          <Product />
          <Product />
          <Product />
          <Product />
          <Product />
        </div>
      </div>
    </div>
  );
};

export default Shop;
