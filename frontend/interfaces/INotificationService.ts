export interface INotificationService {
  showSuccess(message: string): void;
  showErrors(errors: string[]): void;
}
