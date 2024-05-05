import React from "react";
import { ALL_PRODUCTS_GET } from "../../api/store/api";
import { ProductResponse } from "../../api/store/models";
import addIcon from "../../assets/add-icon.svg";
import ProductModal from "../Modals/ProdutModal";
import Product from "./Product";

type Props = {
  isBroadcaster?: boolean;
  live?: Live;
};

const Shop = (props: Props) => {
  const [modalIsOpen, setModalIsOpen] = React.useState(false);
  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };
  const { isBroadcaster, live } = props;

  // Get all Lives
  const sortLiveByCreatedAt = (
    productArray: ProductResponse[]
  ): ProductResponse[] => {
    return productArray.sort((a, b) => {
      const dateA = new Date(a.createdAt);
      const dateB = new Date(b.createdAt);
      return dateB.getTime() - dateA.getTime();
    });
  };
  const [productList, setProductList] = React.useState([] as ProductResponse[]);
  React.useEffect(() => {
    const fetchLiveList = async () => {
      try {
        const { url, options } = ALL_PRODUCTS_GET(live?.slug);
        const response = await fetch(url, options);
        const data: ProductResponse[] = await response.json();

        const sortedData = sortLiveByCreatedAt(data);
        setProductList(sortedData);
      } catch (err) {}
    };
    fetchLiveList();
  }, [modalIsOpen]);

  return (
    <>
      {modalIsOpen ? (
        <ProductModal
          isDelete={false}
          live={live}
          cancelOnClick={handleModalCancelClick}
        />
      ) : (
        <></>
      )}
      <div className="shop">
        <div className="shop-header">
          {isBroadcaster ? (
            <button
              className="btn btn-add"
              onClick={() => setModalIsOpen(true)}
            >
              <div className="text">
                <img src={addIcon} alt="" />
                Add
              </div>
            </button>
          ) : (
            <div className="n-products">{productList.length} products</div>
          )}
        </div>
        <div className="products-container">
          <div className="grid">
            {productList.map((product) => (
              <Product
                isBroadcaster={isBroadcaster}
                productResponse={product}
              />
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default Shop;
