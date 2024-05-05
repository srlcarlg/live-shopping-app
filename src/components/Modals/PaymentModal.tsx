import React from "react";
import { PAYMENT_REQUEST_POST } from "../../api/store/api";
import {
  PaymentRequest,
  ProductResponse,
  TransactionPayment,
} from "../../api/store/models";
import successfulIcon from "../../assets/successful.svg";
import InputModal from "./Input";
import ProductCount from "./ProductCount";

type Props = {
  cancelOnClick: (value: boolean) => void;
  productResponse?: ProductResponse;
};

const PaymentModal = (props: Props) => {
  const [successful, setSuccessful] = React.useState(false);
  const [transaction, setTransaction] = React.useState<TransactionPayment>();

  const [cancelClick, setCancelClick] = React.useState(false);
  const handleCancel = () => {
    props.cancelOnClick(cancelClick);
    setCancelClick(!cancelClick);
  };

  const [formData, setFormData] = React.useState({
    name: "Random Name",
    number: "123456789",
    expiration: "12/24",
    CVV: "123",
  });
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const { productResponse } = props;

  const [parentCount, setParentCount] = React.useState<number>(1);
  const handleCounterChange = (newValue: number) => {
    setParentCount(newValue);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const isValidExpiry = /^\d*(\/)?\d*$/.test(formData.expiration);
    const parsedNumber = parseInt(formData.number, 20);
    const parsedCVV = parseInt(formData.CVV, 10);
    if (
      isValidExpiry &&
      formData.expiration.length === 5 &&
      formData.expiration.charAt(2) === "/" &&
      !isNaN(parsedNumber) &&
      !isNaN(parsedCVV)
    ) {
      try {
        const parts = formData.expiration.split("/");
        const month = parseInt(parts[1], 10);
        const year = parseInt(parts[1], 10);

        // Payment Request
        const requestBody: PaymentRequest = {
          productId: productResponse?.id,
          quantity: parentCount,
          name: formData.name,
          number: parsedNumber,
          expirationMonth: month,
          expirationYear: year,
          CVV: parsedCVV,
        };
        const { url, options } = PAYMENT_REQUEST_POST(
          productResponse?.liveSlug,
          requestBody
        );

        const response = await fetch(url, options);
        if (response.ok) {
          const data: TransactionPayment = await response.json();
          setTransaction(data);
          setSuccessful(true);
        }
      } catch (error) {
        console.log(error);
      }
    } else {
      // Handle invalid input
      alert(
        `Please enter a valid number in \n${
          isNaN(parsedNumber)
            ? "Number"
            : isNaN(parsedCVV)
            ? "CVV"
            : "Month/Year ex: '01/24'"
        }`
      );
    }
  };
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
        {!successful ? (
          <ProductCount
            productResponse={productResponse}
            onValueChange={handleCounterChange}
          />
        ) : (
          <div></div>
        )}
        <div className="modal-inputs">
          <form id="payment-form" onSubmit={handleSubmit}>
            {!successful ? (
              <div className="row-inputs">
                <div className="first-column">
                  <InputModal
                    className="payment-number-input"
                    placeholder="Number"
                    name="number"
                    value={formData.number}
                    onChange={handleChange}
                  />
                  <InputModal
                    className="payment-name-input"
                    placeholder="Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                  />
                </div>
                <div className="colunm">
                  <InputModal
                    className="payment-cvv-input"
                    placeholder="CVV"
                    name="CVV"
                    value={formData.CVV}
                    onChange={handleChange}
                  />
                  <InputModal
                    className="payment-expdate-input"
                    placeholder="Month/Year"
                    name="expiration"
                    value={formData.expiration}
                    onChange={handleChange}
                  />
                </div>
              </div>
            ) : (
              <div></div>
            )}
            {!successful ? (
              <div className="btn-area">
                <button className="btn btn-cancel" onClick={handleCancel}>
                  Cancel
                </button>
                <button
                  form="payment-form"
                  type="submit"
                  className="btn btn-payment-submit"
                >
                  Confirm Payment
                </button>
              </div>
            ) : (
              <>
                <div className="transaction-text">
                  Transaction Id:
                  <p>{transaction?.transactionId} n</p>
                </div>
                <div className="btn-area">
                  <button className="btn btn" onClick={handleCancel}>
                    Exit
                  </button>
                </div>
              </>
            )}
          </form>
        </div>
      </div>
    </div>
  );
};

export default PaymentModal;
