export const STORE_API_URL = `${await import.meta.env.VITE_STORE_API_URL}/store`;

export function ALL_PRODUCTS_GET(slug: string | undefined) {
  return {
    url: STORE_API_URL + '/' + slug,
    options: {
      method: 'GET'
    },
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json"
    },
  };
}

export function CREATE_PRODUCT_POST(slug: string | undefined, body: any) {
  return {
    url: STORE_API_URL + '/' + slug,
    options: {
      method: 'POST',
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body),
    },
  };
}

export function UPDATE_PRODUCT_PUT(slug: string | undefined, productId: any, body: any) {
  return {
    url: STORE_API_URL + '/' + slug + '/' + productId,
    options: {
      method: 'PUT',
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body),
    },
  };
}

export function DELETE_PRODUCT_DEL(
    slug: string | undefined, productId: any, sessionId: any) {
  return {
    url: `${STORE_API_URL}/${slug}/${sessionId}/${productId}`,
    options: {
      method: 'DELETE',
    },
    headers: {
      "Access-Control-Allow-Origin": "*"
    },
  };
}

export function PAYMENT_REQUEST_POST(slug: string | undefined, body: any) {
  return {
    url: `${STORE_API_URL}/${slug}/payment`,
    options: {
      method: 'POST',
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body),
    },
  };
}
