export interface INavigationService {
  navigateTo(page: string): void;
  loadPageContent(page: string): string;
}
