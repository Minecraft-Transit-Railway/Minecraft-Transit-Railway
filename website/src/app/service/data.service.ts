import {Injectable} from "@angular/core";
import {ResourceWrapperDTO} from "../entity/generated/resourceWrapper";
import {HttpClient} from "@angular/common/http";
import {catchError, EMPTY, Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class DataService {
	private resourceWrapperDTO?: ResourceWrapperDTO;
	private status: "loading" | "ok" | "error" = "loading";

	constructor(private readonly httpClient: HttpClient) {
		this.sendGetRequest("operation/refresh");
	}

	public update(id?: string, skipResourceWrapperSync = false) {
		const tempResourceWrapperDTO = this.resourceWrapperDTO;
		if (this.resourceWrapperDTO) {
			const resourceWrapperDTOCopy: ResourceWrapperDTO = JSON.parse(JSON.stringify(this.resourceWrapperDTO));
			resourceWrapperDTOCopy.minecraftModelResources.length = 0;
			resourceWrapperDTOCopy.minecraftTextureResources.length = 0;
			this.sendPostRequest(`operation/update?${id ? `id=${id}` : ""}`, resourceWrapperDTOCopy, "text/plain", () => {
				if (skipResourceWrapperSync) {
					this.resourceWrapperDTO = tempResourceWrapperDTO;
				}
			});
		}
	}

	public create() {
		this.resourceWrapperDTO = new ResourceWrapperDTO();
	}

	public reset() {
		this.resourceWrapperDTO = undefined;
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
		return this.resourceWrapperDTO?.vehicles ?? [];
	}

	public models() {
		return this.resourceWrapperDTO?.modelResources ?? [];
	}

	public textures() {
		return this.resourceWrapperDTO?.textureResources ?? [];
	}

	public minecraftModelResources() {
		return this.resourceWrapperDTO?.minecraftModelResources ?? [];
	}

	public minecraftTextureResources() {
		return this.resourceWrapperDTO?.minecraftTextureResources ?? [];
	}

	public getStatus() {
		return this.status;
	}

	public hasData() {
		return !!this.resourceWrapperDTO;
	}

	public isMinecraftPaused() {
		return this.resourceWrapperDTO?.isMinecraftPaused ?? true;
	}

	public getExportDirectory() {
		return this.resourceWrapperDTO?.exportDirectory ?? "";
	}

	public sendGetRequest(endpoint: string, callback?: () => void) {
		this.sendRequest(this.httpClient.get<ResourceWrapperDTO>(DataService.getUrl(endpoint)), callback);
	}

	public sendPostRequest(endpoint: string, body: any, contentType?: string, callback?: () => void) {
		this.sendRequest(this.httpClient.post<ResourceWrapperDTO>(DataService.getUrl(endpoint), body, contentType ? {headers: {"content-type": contentType}} : undefined), callback);
	}

	private sendRequest(request: Observable<ResourceWrapperDTO>, callback?: () => void) {
		request.pipe(catchError(() => {
			this.status = "error";
			return EMPTY;
		})).subscribe(data => {
			this.status = "ok";
			this.resourceWrapperDTO = data;
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
