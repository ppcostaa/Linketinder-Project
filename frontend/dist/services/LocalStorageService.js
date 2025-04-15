export class LocalStorageService {
    save(key, data) {
        localStorage.setItem(key, JSON.stringify(data));
    }
    load(key) {
        return JSON.parse(localStorage.getItem(key) || "[]");
    }
}
