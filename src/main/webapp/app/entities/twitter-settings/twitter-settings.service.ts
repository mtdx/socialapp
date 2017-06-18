import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TwitterSettings } from './twitter-settings.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TwitterSettingsService {

    private resourceUrl = 'api/twitter-settings';

    constructor(private http: Http) { }

    update(twitterSettings: TwitterSettings): Observable<TwitterSettings> {
        const copy = this.convert(twitterSettings);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(): Observable<TwitterSettings> {
        return this.http.get(this.resourceUrl).map((res: Response) => {
            return res.json();
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(twitterSettings: TwitterSettings): TwitterSettings {
        const copy: TwitterSettings = Object.assign({}, twitterSettings);
        return copy;
    }
}
