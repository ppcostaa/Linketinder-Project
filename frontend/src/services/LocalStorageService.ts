import { IStorageService } from "../interfaces/IStorageService";

export class LocalStorageService implements IStorageService {
  save(key: string, data: any): void {
    localStorage.setItem(key, JSON.stringify(data));
  }

  load(key: string): any {
    return JSON.parse(localStorage.getItem(key) || "[]");
  }
}
