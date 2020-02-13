# foreground-detector-scala

Foreground detection is one of the major tasks in the field of computer vision and image processing whose aim is to detect changes in image sequences. Background subtraction is any technique which allows an image's foreground to be extracted for further processing (object recognition etc.).

## The algorithm: mean filter

For calculating the image containing only the background, a series of preceding images are averaged. For calculating the background image at the instant t,

![B(x,y,t)={1 \over N}\sum _{i=1}^{N}V(x,y,t-i)](https://wikimedia.org/api/rest_v1/media/math/render/svg/6cfa7c59f7d2395f3ff84b0d76df2a5394a00450)

where *N* is the number of preceding images taken for averaging. This averaging refers to averaging corresponding pixels in the given images. *N* would depend on the video speed (number of images per second in the video) and the amount of movement in the video. After calculating the background *B*(*x*,*y*,*t*) we can then subtract it from the image *V*(*x*,*y*,*t*) at time *t* = t and threshold it. Thus the foreground is

![{\displaystyle |V(x,y,t)-B(x,y,t)|>\mathrm {Th} }](https://wikimedia.org/api/rest_v1/media/math/render/svg/82a966d874d89977f08cf97357da75b82b452fc1)

where Th is threshold. Similarly we can also use median instead of mean in the above calculation of *B*(*x*,*y*,*t*).

## How to run

```
sbt "run ForegroundDectectorApp [video_absolute_path]"
```

Source : [Wikipedia](https://en.wikipedia.org/)