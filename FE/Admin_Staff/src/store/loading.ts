export interface LoadingState {
  isLoading: boolean;
  error: string | null;
}

export const initialLoading: LoadingState = {
  isLoading: false,
  error: null,
};
