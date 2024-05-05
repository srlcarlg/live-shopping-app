import React from "react";
import { ProductResponse } from "../../api/store/models";
import buyIcon from "../../assets/buy-icon.svg";
import editIcon from "../../assets/edit-icon.svg";
import excludeIcon from "../../assets/exclude-icon.svg";
import productImg from "../../assets/product-1.png";
import timeIcon from "../../assets/time-icon.svg";
import PaymentModal from "../Modals/PaymentModal";
import ProductModal from "../Modals/ProdutModal";

type Props = {
  isBroadcaster?: boolean;
  productResponse?: ProductResponse;
};

const Product = (props: Props) => {
  const { isBroadcaster, productResponse } = props;

  const [modalIsOpen, setModalIsOpen] = React.useState(false);
  const [modalIsDelete, setModalIsDelete] = React.useState(false);

  const handleModalOpening = (isDelete: boolean) => {
    setModalIsDelete(isDelete);
    setModalIsOpen(!modalIsOpen);
  };
  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };

  return (
    <>
      {modalIsOpen ? (
        <ProductModal
          productResponse={productResponse}
          isDelete={modalIsDelete}
          cancelOnClick={handleModalCancelClick}
        />
      ) : (
        <></>
      )}
      {modalIsOpen && !isBroadcaster ? (
        <PaymentModal
          productResponse={productResponse}
          cancelOnClick={handleModalCancelClick}
        />
      ) : (
        <></>
      )}
      <div key={productResponse?.liveSlug} className="product-container">
        <img src={productImg} alt="" />
        <div className="product-info">
          <div className="text-column">
            {productResponse?.title}
            <span className="price">${productResponse?.price}</span>
          </div>
          <div className="button-column">
            <div
              className={
                isBroadcaster ? "btn timeleft broadcaster" : "btn timeleft"
              }
            >
              <div className="text">
                <img src={timeIcon} alt="" />
                {productResponse?.timeLeft}m left
              </div>
            </div>
            <p>
              <span className="units">Units:</span> {productResponse?.quantity}
            </p>
            {!isBroadcaster ? (
              <button
                className="btn btn-buy"
                onClick={() => {
                  handleModalOpening(false);
                }}
              >
                <div className="buy-text">
                  <img src={buyIcon} alt="" />
                  Buy Now
                </div>
              </button>
            ) : (
              <div className="broadcaster-row">
                <button
                  className="btn btn-delete"
                  onClick={() => {
                    handleModalOpening(true);
                  }}
                >
                  <img src={excludeIcon} alt="" />
                </button>
                <button
                  className="btn btn-edit"
                  onClick={() => {
                    handleModalOpening(false);
                  }}
                >
                  <div className="">
                    <img src={editIcon} alt="" />
                  </div>
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default Product;
