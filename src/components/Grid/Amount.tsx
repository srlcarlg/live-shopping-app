import React from "react";
import { SSE_TOTAL_PER_LIVE } from "../../api/api";
import AmountInfo from "./AmountInfo";

type Props = {};

const Amount = (props: Props) => {
  // Server-Side-Events
  const [amountList, setAmountList] = React.useState([] as Amount[]);
  React.useEffect(() => {
    const eventSource = new EventSource(SSE_TOTAL_PER_LIVE);

    eventSource.onmessage = (event: MessageEvent) => {
      const data: Amount = JSON.parse(event.data);
      if (amountList.some((t) => t.liveSlug === data.liveSlug)) {
        setAmountList(
          amountList.map((entity) =>
            entity.liveSlug === data.liveSlug
              ? {
                  ...entity,
                  nTransactions: data.nTransactions,
                  total: data.total,
                }
              : entity
          )
        );
      } else {
        setAmountList((prevDataArray) => [...prevDataArray, data]);
      }
    };

    eventSource.onerror = (error: Event) => {
      console.error("SSE Error:", error);
    };

    return () => {
      eventSource.close();
    };
  });

  const sortByTotal = (list: Amount[]): Amount[] => {
    return list.sort((a, b) => {
      return a.total > b.total ? a.total : b.total;
    });
  };

  return (
    <div className="all-transactions">
      <div className="transactions-header">
        <h3>Total Amount per Live</h3>
      </div>
      <div className="table-grid">
        <table>
          <thead className="table-header">
            <tr>
              <td>Live Slug</td>
              <td>nº Transactions</td>
              <td>Total</td>
            </tr>
          </thead>
          <tbody>
            {sortByTotal(amountList).map((amount, index) => (
              <tr key={index}>
                <AmountInfo amount={amount} />
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Amount;
