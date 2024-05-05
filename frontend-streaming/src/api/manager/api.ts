export const MANAGER_API_URL = `${await import.meta.env.VITE_MANAGER_API_URL}/lives`;
export const MANAGER_WS_URL = `ws://${await import.meta.env.VITE_MANAGER_URL_NO_HTTP}/live`;

export function FIND_LIVE_GET(liveSlug: string) {
  return {
    url: MANAGER_API_URL + '/' + liveSlug,
    options: {
      method: 'GET'
    },
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  };
}