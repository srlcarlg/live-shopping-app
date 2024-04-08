import React from "react";
import buyIcon from "../../assets/buy-icon.svg";
import editIcon from "../../assets/edit-icon.svg";
import excludeIcon from "../../assets/exclude-icon.svg";
import productImg from "../../assets/product-1.png";
import timeIcon from "../../assets/time-icon.svg";

type Props = {};

const Product = (props: Props) => {
  const [isBroadcaster, setIsBroadcaster] = React.useState(true);

  return (
    <div className="product-container">
      <img src={productImg} alt="" />
      <div className="product-info">
        <div className="text-column">
          The great headset Stereo/5.1/7.1
          <span className="price">$30.14</span>
        </div>
        <div className="button-column">
          <div
            className={
              isBroadcaster ? "btn timeleft broadcaster" : "btn timeleft"
            }
          >
            <div className="text">
              <img src={timeIcon} alt="" />
              18:3m left
            </div>
          </div>
          <p>
            <span className="units">Units:</span> 2
          </p>
          {!isBroadcaster ? (
            <button className="btn btn-buy">
              <div className="buy-text">
                <img src={buyIcon} alt="" />
                Buy Now
              </div>
            </button>
          ) : (
            <div className="broadcaster-row">
              <button className="btn btn-delete">
                <img src={excludeIcon} alt="" />
              </button>
              <button className="btn btn-edit">
                <div className="">
                  <img src={editIcon} alt="" />
                </div>
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Product;
