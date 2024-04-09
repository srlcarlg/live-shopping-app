import React from "react";
import InputModal from "./Input";

type Props = {};

const ProductModal = (props: Props) => {
  const [isDelete, setIsDelete] = React.useState(false);

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>{!isDelete ? "Add-Edit Product" : "Delete"}</h3>
          {!isDelete ? <div></div> : <h4>The great headset?</h4>}
        </div>
        <div className="modal-inputs">
          {!isDelete ? (
            <div className="row-inputs">
              <div className="row">
                <InputModal
                  className="product-title-input"
                  placeholder="Title"
                  onChange={() => {}}
                />
                <div className="inner-row">
                  <InputModal
                    className="product-image-input"
                    placeholder="Image URL"
                    onChange={() => {}}
                  />
                  <InputModal
                    className="product-timeleft-input"
                    placeholder="Time Left"
                    onChange={() => {}}
                  />
                </div>
              </div>
              <div className="colunm">
                <InputModal
                  className="product-price-input"
                  placeholder="Price"
                  onChange={() => {}}
                />
                <InputModal
                  className="product-quantity-input"
                  placeholder="Quantity"
                  onChange={() => {}}
                />
              </div>
            </div>
          ) : (
            <div></div>
          )}
          <div className="btn-area">
            <button className="btn btn-cancel">Cancel</button>
            {!isDelete ? (
              <button type="submit" className="btn btn-product-submit">
                Confirm
              </button>
            ) : (
              <button type="submit" className="btn btn-product-delete">
                Delete
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductModal;
