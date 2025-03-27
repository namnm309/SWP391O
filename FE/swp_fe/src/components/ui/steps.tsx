import * as React from "react";
import { cn } from "@/lib/utils";

interface StepsProps extends React.HTMLAttributes<HTMLDivElement> {
  currentStep?: number;
  children: React.ReactNode;
}

export function Steps({
  currentStep = 0,
  className,
  children,
  ...props
}: StepsProps) {
  const steps = React.Children.toArray(children);
  const totalSteps = steps.length;

  return (
    <div className={cn("w-full", className)} {...props}>
      <div className="flex w-full items-center">
        {steps.map((step, index) => {
          const isActive = index <= currentStep;
          const isLastStep = index === totalSteps - 1;

          return (
            <React.Fragment key={index}>
              <div className="relative flex flex-1 flex-col items-center">
                {React.isValidElement<StepProps>(step) &&
                  React.cloneElement<StepProps>(step, { isActive })}
              </div>
              {!isLastStep && (
                <div
                  className={cn(
                    "h-1 flex-1 bg-gray-200 transition-colors duration-300",
                    isActive && index < currentStep ? "bg-primary" : ""
                  )}
                />
              )}
            </React.Fragment>
          );
        })}
      </div>
    </div>
  );
}

interface StepProps extends React.HTMLAttributes<HTMLDivElement> {
  icon?: React.ElementType;
  title: string;
  description?: string;
  isActive?: boolean;
}

export function Step({
  icon: Icon,
  title,
  description,
  isActive = false,
  className,
  ...props
}: StepProps) {
  return (
    <div
      className={cn("flex flex-col items-center text-center", className)}
      {...props}
    >
      <div
        className={cn(
          "flex h-10 w-10 items-center justify-center rounded-full border-2 border-gray-200 bg-white transition-colors duration-300",
          isActive ? "border-primary bg-primary text-white" : "text-gray-400"
        )}
      >
        {Icon && <Icon className="h-5 w-5" />}
      </div>
      <div className="mt-2">
        <div
          className={cn(
            "text-sm font-medium",
            isActive ? "text-gray-900" : "text-gray-500"
          )}
        >
          {title}
        </div>
        {description && (
          <div className="text-xs text-gray-500">{description}</div>
        )}
      </div>
    </div>
  );
}
