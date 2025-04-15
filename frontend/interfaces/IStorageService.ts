export interface IStorageService {
  save(key: string, data: any): void;
  load(key: string): any;
}
