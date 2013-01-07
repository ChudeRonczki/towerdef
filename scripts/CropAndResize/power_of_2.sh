#!/bin/bash

f="tiles.png"
p2w=`convert $f -format "%[fx:2^(ceil(log(w)/log(2)))]" info:`
p2h=`convert $f -format "%[fx:2^(ceil(log(h)/log(2)))]" info:`
convert $f -background transparent -define png:format=png32 -extent ${p2w}x${p2h} $f