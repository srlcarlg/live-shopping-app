import React from "react";
import { SSE_TRANSACTIONS_URL } from "../../api/api";
import TransactionsInfo from "./TransactionsInfo";

type Props = {};

const Transactions = (props: Props) => {
  
  // Server-Side-Events
  const [transactionList, setTransactionList] = React.useState(
    [] as Transaction[]
  );
  React.useEffect(() => {
    const eventSource = new EventSource(SSE_TRANSACTIONS_URL);

    eventSource.onmessage = (event: MessageEvent) => {
      const data = JSON.parse(event.data);
      if (
        transactionList.length === 0 ||
        !transactionList.some((t) => isEqual(data, t))
      ) {
        setTransactionList((prevDataArray) => [...prevDataArray, data]);
      }
    };

    eventSource.onerror = (error: Event) => {
      console.error("SSE Error:", error);
    };

    return () => {
      eventSource.close();
    };
  });

  const isEqual = (obj1: Transaction, obj2: Transaction): boolean => {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  };
  const sortByCreatedAt = (transactionList: Transaction[]): Transaction[] => {
    return transactionList.sort((a, b) => {
      const dateA = new Date(a.createdAt);
      const dateB = new Date(b.createdAt);
      return dateB.getTime() - dateA.getTime();
    });
  };

  return (
    <div className="all-transactions">
      <div className="transactions-header">
        <h3>Latest Transactions</h3>
      </div>
      <div className="table-grid">
        <table>
          <thead className="table-header">
            <tr>
              <td>UUID</td>
              <td>Live Slug</td>
              <td>Date</td>
              <td>Amount</td>
            </tr>
          </thead>
          <tbody>
            {sortByCreatedAt(transactionList).map((transaction, index) => (
              <tr key={index}>
                <TransactionsInfo transaction={transaction} />
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Transactions;
