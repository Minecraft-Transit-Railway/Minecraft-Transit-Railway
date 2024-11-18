import {Injectable} from "@angular/core";
import {ResourceWrapper} from "../entity/generated/resourceWrapper";
import {HttpClient} from "@angular/common/http";
import {catchError, EMPTY, Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class DataService {
	private resourceWrapper?: ResourceWrapper;
	private status: "loading" | "ok" | "error" = "loading";

	constructor(private readonly httpClient: HttpClient) {
		this.sendGetRequest("operation/refresh");
	}

	public update(skipResourceWrapperSync = false) {
		const tempResourceWrapper = this.resourceWrapper;
		if (this.resourceWrapper) {
			const resourceWrapperCopy: ResourceWrapper = JSON.parse(JSON.stringify(this.resourceWrapper));
			resourceWrapperCopy.minecraftModelResources.length = 0;
			resourceWrapperCopy.minecraftTextureResources.length = 0;
			this.sendPostRequest("operation/update", resourceWrapperCopy, "text/plain", () => {
				if (skipResourceWrapperSync) {
					this.resourceWrapper = tempResourceWrapper;
				}
			});
		}
	}

	public create() {
		this.resourceWrapper = new ResourceWrapper();
	}

	public reset() {
		this.resourceWrapper = undefined;
		this.sendGetRequest("upload/reset");
	}

	public preview(openDoors: boolean) {
		this.sendGetRequest(`operation/preview?doors=${openDoors ? "open" : "close"}`);
	}

	public reload() {
		this.sendGetRequest("operation/force-reload");
	}

	public resumeGame() {
		this.sendGetRequest("operation/resume-game");
	}

	public export(name: string, description: string) {
		this.sendGetRequest(`upload/export?name=${encodeURIComponent(name)}&description=${encodeURIComponent(description)}`);
	}

	public vehicles() {
		return this.resourceWrapper?.vehicles ?? [];
	}

	public models() {
		return this.resourceWrapper?.modelResources ?? [];
	}

	public textures() {
		return this.resourceWrapper?.textureResources ?? [];
	}

	public minecraftModelResources() {
		return this.resourceWrapper?.minecraftModelResources ?? [];
	}

	public minecraftTextureResources() {
		return this.resourceWrapper?.minecraftTextureResources ?? [];
	}

	public getStatus() {
		return this.status;
	}

	public hasData() {
		return !!this.resourceWrapper;
	}

	public isMinecraftPaused() {
		return this.resourceWrapper?.isMinecraftPaused ?? true;
	}

	public getExportDirectory() {
		return this.resourceWrapper?.exportDirectory ?? "";
	}

	public sendGetRequest(endpoint: string, callback?: () => void) {
		this.sendRequest(this.httpClient.get<ResourceWrapper>(DataService.getUrl(endpoint)), callback);
	}

	public sendPostRequest(endpoint: string, body: any, contentType?: string, callback?: () => void) {
		this.sendRequest(this.httpClient.post<ResourceWrapper>(DataService.getUrl(endpoint), body, contentType ? {headers: {"content-type": contentType}} : undefined), callback);
	}

	private sendRequest(request: Observable<ResourceWrapper>, callback?: () => void) {
		request.pipe(catchError(() => {
			this.status = "error";
			return EMPTY;
		})).subscribe(data => {
			this.status = "ok";
			this.resourceWrapper = data;
			if (callback) {
				callback();
			}
		});
	}

	public static getUrl(endpoint: string) {
		const pathName = document.location.pathname;
		return `${document.location.origin}${pathName.substring(0, pathName.length - 8)}mtr/api/creator/${endpoint}`;
	}
}
