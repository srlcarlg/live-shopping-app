export const MANAGER_API_URL = `${await import.meta.env.VITE_MANAGER_API_URL}/lives`;
export const STORE_API_URL = `${await import.meta.env.VITE_STORE_API_URL}/store`;

export const SSE_TRANSACTIONS_URL = STORE_API_URL  + '/transactions';
export const SSE_TOTAL_PER_LIVE = STORE_API_URL + '/total'

export function CREATE_LIVE_POST(body: any) {
  return {
    url: MANAGER_API_URL,
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

export function EDIT_LIVE_PASSWORD_PUT(liveSlug: string, body: any) {
  return {
    url: MANAGER_API_URL + '/' + liveSlug,
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

export function ALL_LIVES_GET() {
  return {
    url: MANAGER_API_URL,
    options: {
      method: 'GET'
    },
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  };
}