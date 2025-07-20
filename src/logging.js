export function errorHandler(msg) {
  return (error) => {
    console.error(`[yank] ${msg} with error: ${error}`);
  }
}
