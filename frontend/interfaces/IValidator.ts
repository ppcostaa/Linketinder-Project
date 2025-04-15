export interface IValidator {
  validate(data: any): { isValid: boolean; errors: string[] };
}
