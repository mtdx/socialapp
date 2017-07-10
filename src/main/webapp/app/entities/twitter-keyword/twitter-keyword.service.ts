import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { TwitterKeyword } from './twitter-keyword.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TwitterKeywordService {

    private resourceUrl = 'api/twitter-keywords';
    private resourceSearchUrl = 'api/_search/twitter-keywords';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(twitterKeyword: TwitterKeyword): Observable<TwitterKeyword> {
        const copy = this.convert(twitterKeyword);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(twitterKeyword: TwitterKeyword): Observable<TwitterKeyword> {
        const copy = this.convert(twitterKeyword);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TwitterKeyword> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.created = this.dateUtils
            .convertDateTimeFromServer(entity.created);
    }

    private convert(twitterKeyword: TwitterKeyword): TwitterKeyword {
        const copy: TwitterKeyword = Object.assign({}, twitterKeyword);

        copy.created = this.dateUtils.toDate(twitterKeyword.created);
        return copy;
    }
}
