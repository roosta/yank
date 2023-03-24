export function errorHandler(msg) {
  return (error) => {
    console.log(`[yank] ${msg} with error: ${error}`);
  }
}
