import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
  children: JSX.Element;
  isAllowed: boolean;
  redirectPath?: string;
}

const ProtectedRoute = ({
  children,
  isAllowed,
  redirectPath = "/",
}: ProtectedRouteProps) => {
  if (!isAllowed) {
    return <Navigate to={redirectPath} />;
  }

  return children;
};

export default ProtectedRoute;
