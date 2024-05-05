import React from "react";
import {
  CREATE_PRODUCT_POST,
  DELETE_PRODUCT_DEL,
  UPDATE_PRODUCT_PUT,
} from "../../api/store/api";
import { ProductResponse } from "../../api/store/models";
import { useSession } from "../../context/SessionContext";
import InputModal from "./Input";

type Props = {
  isDelete: boolean;
  productResponse?: ProductResponse;
  live?: Live;
  cancelOnClick: (value: boolean) => void;
};

const ProductModal = (props: Props) => {
  const [cancelClick, setCancelClick] = React.useState(false);
  const handleCancel = () => {
    props.cancelOnClick(cancelClick);
    setCancelClick(!cancelClick);
  };

  const { live, isDelete, productResponse } = props;
  const { sessionId } = useSession();

  const [formData, setFormData] = React.useState({
    sessionId: sessionId ? sessionId : "",
    title: productResponse
      ? productResponse?.title.toString()
      : "The great headset Stereo/5.1/7.1",
    price: productResponse ? productResponse?.price.toString() : "30.14",
    quantity: productResponse ? productResponse?.quantity.toString() : "4",
    timeLeft: productResponse ? productResponse?.timeLeft.toString() : "5",
    imageUrl: productResponse
      ? productResponse?.imageUrl.toString()
      : "some-image-url",
  });
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
      sessionId: sessionId ? sessionId : "",
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const parsedPrice = parseFloat(formData.price);
    const parsedQuantity = parseInt(formData.quantity, 10);
    const parsedTimeLeft = parseInt(formData.timeLeft, 10);
    if (
      !isNaN(parsedPrice) &&
      !isNaN(parsedQuantity) &&
      !isNaN(parsedTimeLeft)
    ) {
      try {
        if (!productResponse) {
          // Add product
          const { url, options } = CREATE_PRODUCT_POST(live?.slug, formData);
          const response = await fetch(url, options);
          const data: ProductResponse = await response.json();

          if (response.ok) {
            handleCancel();
            console.log(data);
          }
        } else {
          // Edit Product
          const { url, options } = UPDATE_PRODUCT_PUT(
            productResponse?.liveSlug,
            productResponse?.id,
            formData
          );
          const response = await fetch(url, options);
          const data: ProductResponse = await response.json();

          if (response.ok) {
            handleCancel();
            console.log(data);
          }
        }
      } catch (error) {
        console.log(error);
      }
    } else {
      // Handle invalid input
      alert(
        `Please enter a valid number in \n${
          isNaN(parsedPrice)
            ? "Price"
            : isNaN(parsedQuantity)
            ? "Quantity"
            : "TimeLeft"
        }`
      );
    }
  };

  const handleDelete = async () => {
    // Delete Product
    const { url, options } = DELETE_PRODUCT_DEL(
      productResponse?.liveSlug,
      productResponse?.id,
      sessionId
    );
    await fetch(url, options);
    handleCancel();
  };

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>
            {isDelete
              ? "Delete"
              : productResponse
              ? "Edit Product"
              : "AddProduct"}
          </h3>
          {!isDelete ? <div></div> : <h4>{productResponse?.title}?</h4>}
        </div>
        <div className="modal-inputs">
          <form id="product-form" onSubmit={handleSubmit}>
            {!isDelete ? (
              <div className="row-inputs">
                <div className="row">
                  <InputModal
                    className="product-title-input"
                    placeholder="Title"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                  />
                  <div className="inner-row">
                    <InputModal
                      className="product-image-input"
                      placeholder="Image URL"
                      name="imageUrl"
                      value={formData.imageUrl}
                      onChange={handleChange}
                    />
                    <InputModal
                      className="product-timeleft-input"
                      placeholder="Time Left"
                      name="timeLeft"
                      value={formData.timeLeft}
                      onChange={handleChange}
                    />
                  </div>
                </div>
                <div className="colunm">
                  <InputModal
                    className="product-price-input"
                    placeholder="Price"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                  />
                  <InputModal
                    className="product-quantity-input"
                    placeholder="Quantity"
                    name="quantity"
                    value={formData.quantity}
                    onChange={handleChange}
                  />
                </div>
              </div>
            ) : (
              <div></div>
            )}
            <div className="btn-area">
              <button className="btn btn-cancel" onClick={handleCancel}>
                Cancel
              </button>
              {!isDelete ? (
                <button
                  type="submit"
                  form="product-form"
                  className="btn btn-product-submit"
                >
                  Confirm
                </button>
              ) : (
                <button
                  onClick={handleDelete}
                  className="btn btn-product-delete"
                >
                  Delete
                </button>
              )}
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ProductModal;
