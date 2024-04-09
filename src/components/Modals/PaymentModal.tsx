import React from "react";
import successfulIcon from "../../assets/successful.svg";
import InputModal from "./Input";
import ProductCount from "./ProductCount";

type Props = {};

const PaymentModal = (props: Props) => {
  const [successful, setSuccessful] = React.useState(false);

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          {!successful ? (
            <h3>Payment Details</h3>
          ) : (
            <div className="title">
              <img src={successfulIcon}></img>
              <h4>Successful!</h4>
            </div>
          )}
        </div>
        {!successful ? <ProductCount /> : <div></div>}
        <div className="modal-inputs">
          {!successful ? (
            <div className="row-inputs">
              <div className="first-column">
                <InputModal
                  className="payment-number-input"
                  placeholder="Number"
                  onChange={() => {}}
                />
                <InputModal
                  className="payment-name-input"
                  placeholder="Name"
                  onChange={() => {}}
                />
              </div>
              <div className="colunm">
                <InputModal
                  className="payment-cvv-input"
                  placeholder="CVV"
                  onChange={() => {}}
                />
                <InputModal
                  className="payment-expdate-input"
                  placeholder="Expiry Date"
                  onChange={() => {}}
                />
              </div>
            </div>
          ) : (
            <div></div>
          )}
          {!successful ? (
            <div className="btn-area">
              <button className="btn btn-cancel">Cancel</button>
              <button type="submit" className="btn btn-payment-submit">
                Confirm Payment
              </button>
            </div>
          ) : (
            <div className="transaction-text">
              Transaction Id:
              <p>4798f72f-6fd4-4671-6425f0a5de59</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PaymentModal;
